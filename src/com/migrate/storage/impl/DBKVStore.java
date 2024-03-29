package com.migrate.storage.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.migrate.rest.VersionMismatchException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;


import com.migrate.exception.DuplicationKeyException;
import com.migrate.exception.StorageFailureException;
import com.migrate.storage.KVObject;
import com.migrate.storage.KVStore;

/**
 * @author Zane Pan
 */
@Transactional(isolation=Isolation.REPEATABLE_READ)
public class DBKVStore implements KVStore {
    private static org.apache.log4j.Logger log = Logger.getLogger(DBKVStore.class);
    private static final String getSql = "select dataKey, className, value, updateTime, version, deleted from kv_table where namespace=? and dataKey = ? and tenantId = ?";
    private static final String getWithVersionSql = getSql + " and version = ?";

    // fully delete the item and its data
    private static final String DELETE_SQL = "delete from kv_table where namespace=? and dataKey = ? and tenantId = ?";

    // Leaves item as a "deleted" marker but drops its data, eventually will need to track clients that accessed the
    // data before full delete is possible.
    private static final String DELETE_SOFT_UPDATE_SQL = "update kv_table set value = NULL, updateTime=?, deleted=1 where namespace=? and (deleted is NULL or deleted=0) and dataKey=? and tenantId = ?";
    private static final String UPDATE_SQL = "update kv_table set value = ?, updateTime=?, version=? where namespace = ? and dataKey=? and tenantId = ?";
    private static final String UPDATE_WITH_VERSION_SQL = UPDATE_SQL + " and version = ?";
    private static final String INSERT_SQL = "insert into kv_table values (?,?,?,?,?,?,?,?)";
    //	private static final String FIND_SQL = "select namespace, dataKey, className, value, updateTime, version from kv_table where namespace=? and classname = ? and updateTime > ?";
    private static final String FIND_SQL = "select namespace, dataKey, className, value, updateTime, version, deleted from kv_table where (namespace=?) and (classname = ?) and (updateTime > ?) and (tenantId = ?) limit ?,?";

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(BasicDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public KVObject get(String namespace, String key, String tenantId) throws IOException {
        log.info("get namespace:" + namespace + " key:" + key);
        return getDo(namespace, key, getSql, KVObject.ANY_VERSION, tenantId);
    }

    @Override
    public KVObject get(String namespace, String key, long version, String tenantId)
            throws IOException
    {
        return getDo(namespace, key, getWithVersionSql, version, tenantId);
    }

    @Override
    public List<KVObject> findChanged(String namespace, String classname,
                                      long time, int start, int numMatches, String tenantId)
    {
        RowMapper<KVObject> mapper = new RowMapper<KVObject>() {
            public KVObject mapRow(ResultSet rs, int rowNum) throws SQLException {
                KVObject obj = new KVObject();
                obj.setDeleted(rs.getString("deleted"));
                obj.setNamespace(rs.getString("namespace"));
                obj.setDataKey(rs.getString("dataKey"));
                obj.setClassName(rs.getString("className"));
                obj.setUpdateTime(rs.getLong("updateTime"));
                obj.setValue((rs.getBytes("value")));
                obj.setVersion(rs.getLong("version"));
                return obj;
            }
        };

        Object[] args = new Object[] {
                namespace, classname, time, tenantId, start, numMatches
        };
        List<KVObject> list = jdbcTemplate.query(FIND_SQL, args, mapper);
        return list;
    }

    private KVObject getDo(final String namespace, final String key, String sql, long version, String tenantId) throws IOException {
        RowMapper<KVObject> mapper = new RowMapper<KVObject>() {
            public KVObject mapRow(ResultSet rs, int rowNum) throws SQLException {
                KVObject obj = new KVObject();
                obj.setNamespace(namespace);
                obj.setDataKey(rs.getString("dataKey"));
                obj.setClassName(rs.getString("className"));
                obj.setUpdateTime(rs.getLong("updateTime"));
                obj.setValue((rs.getBytes("value")));
                obj.setVersion(rs.getLong("version"));
                return obj;
            }
        };

        KVObject kvo = null;

        try {
            if ( version == KVObject.ANY_VERSION) {
                kvo = jdbcTemplate.queryForObject(sql, mapper, namespace, key, tenantId);
            } else {
                kvo = jdbcTemplate.queryForObject(sql, mapper, namespace, key, version, tenantId);
            }
        } catch ( IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() != 0 ) {
                throw new StorageFailureException(e);
            }
        }
        return kvo;
    }

    @Override
    public void delete(final String namespace, final String key, String tenantId)
            throws IOException
    {
        delete(namespace, key, true, 0L, tenantId);
    }

    @Override
    public void delete(final String namespace, final String key, boolean isPermanent, long now, String tenantId)
            throws IOException
    {
        if (isPermanent) {
            jdbcTemplate.update(DELETE_SQL, namespace, key, tenantId);
        } else {
            jdbcTemplate.update(DELETE_SOFT_UPDATE_SQL, now, namespace, key, tenantId);
        }
    }

    @Override
    public void update(KVObject data, String tenantId) throws IOException {
        updateDo(tenantId, data);
    }

    private void updateDo(String tenantId, KVObject data)  throws IOException {
        long version = data.getVersion();
        data.setVersion(version + 1); // TODO: also need to update version in json?
        int rowUpdated = jdbcTemplate.update(UPDATE_WITH_VERSION_SQL,
                data.getValue(), System.currentTimeMillis(),
                data.getVersion(), data.getNamespace(), data.getKey(),
                tenantId, new Long(version));

        // TODO: throwing cryptic 500 errors to the client absolutely sucks. They need to be
        // more informative, like: the version mismatched, fix your client.

        if (rowUpdated != 1)  {
            // TODO: this needs to get back to client as intelligible error, not as internal 500 error.
            throw new VersionMismatchException("row updated : " + rowUpdated);
        }
    }

    private void insertObject(KVObject data, String tenantId) throws IOException {
        try {
            jdbcTemplate.update(INSERT_SQL,
                    data.getNamespace(), data.getKey(), data.getClassName(),
                    data.getValue(),
                    data.getUpdateTime(),
                    new Long(1),
                    null,
                    tenantId);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new DuplicationKeyException ("namespace: " + data.getNamespace() + " key:" + data.getKey() );
        }
    }

    @Override
    public void create(final String namespace, final String key, String className, byte[] value, String tenantId) throws IOException {
        KVObject kvo = new KVObject(namespace, key, className, value);
        insertObject(kvo, tenantId);
    }

    @Override
    public void put(KVObject data, String tenantId) throws IOException {
        try {
            insertObject(data, tenantId);
        } catch (DuplicationKeyException e) {
            update(data, tenantId);
        }
    }
}
