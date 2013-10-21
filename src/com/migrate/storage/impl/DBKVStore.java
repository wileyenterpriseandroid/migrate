package com.migrate.storage.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.migrate.exception.VersionMismatchException;
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
	private static final String getSql = "select dataKey, className, value, updateTime, version from kv_table where namespace=? and dataKey = ?";
	private static final String getWithVersionSql = getSql + " and version = ?";
	private static final String deleteSql = "delete from kv_table where namespace=? and dataKey = ?";
	private static final String updateSql = "update kv_table set value = ?, updateTime=?, version=? where namespace = ? and datakey=?";
	private static final String updateWithVersionSql = updateSql + " and version = ?";
	private static final String insertSql = "insert into kv_table values (?,?,?,?,?,?)";
//	private static final String findSql = "select namespace, dataKey, className, value, updateTime, version from kv_table where namespace=? and classname = ? and updateTime > ?";
	private static final String findSql = "select namespace, dataKey, className, value, updateTime, version from kv_table where (namespace=?) and (classname = ?) and (updateTime > ?) limit ?,?";

	private JdbcTemplate jdbcTemplate;
 
	public void setDataSource(BasicDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public KVObject get(String namespace, String key) throws IOException {
		log.info("get namespace:" + namespace + " key:" + key);
		return getDo(namespace, key, getSql, KVObject.ANY_VERSION);
	}

	@Override
	public KVObject get(String namespace, String key, long version)
			throws IOException {
		return getDo(namespace, key, getWithVersionSql, version);
	}

	@Override
	public List<KVObject> findChanged(String namespace, String classname,
                                      long time, int start, int numMatches)
    {
		RowMapper<KVObject> mapper = new RowMapper<KVObject>() {
			public KVObject mapRow(ResultSet rs, int rowNum) throws SQLException {
				KVObject obj = new KVObject();
				obj.setNamespace(rs.getString("namespace"));
				obj.setKey(rs.getString("dataKey"));
				obj.setClassName(rs.getString("className"));
				obj.setUpdateTime(rs.getLong("updateTime"));
				obj.setValue((rs.getBytes("value")));
				obj.setVersion(rs.getLong("version"));
				return obj;
			}
		};
	
		Object[] args = new Object[] {namespace, classname, time, start, numMatches};
		List<KVObject> list = jdbcTemplate.query(findSql, args, mapper);
		return list;
	}

	private KVObject getDo(final String namespace, final String key, String sql, long version) throws IOException {
		RowMapper<KVObject> mapper = new RowMapper<KVObject>() {
			public KVObject mapRow(ResultSet rs, int rowNum) throws SQLException {
				KVObject obj = new KVObject();
				obj.setNamespace(namespace);
				obj.setKey(rs.getString("dataKey"));
				obj.setClassName(rs.getString("className"));
				obj.setUpdateTime(rs.getLong("updateTime"));
				obj.setValue((rs.getBytes("value")));
				obj.setVersion(rs.getLong("version"));
				return obj;
			}
		};
		KVObject kvo = null;;
		try {
			if ( version == KVObject.ANY_VERSION) {
				kvo = jdbcTemplate.queryForObject(sql, mapper, namespace, key);
			} else {
				kvo = jdbcTemplate.queryForObject(sql, mapper, namespace, key, version);
			}
		} catch ( IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() != 0 ) {
				throw new StorageFailureException(e);
			}
		}
		return kvo;		
	}

	@Override
	public void delete(final String namespace, final String key) throws IOException {
		jdbcTemplate.update(deleteSql, new Object[] {namespace, key});
	}

	@Override
	public void update(KVObject data)
			throws IOException {
		updateDo(data);
	}

	private void updateDo(KVObject data)  throws IOException {
		long version = data.getVersion();
		data.setVersion(version +1);
		int rowupdated = jdbcTemplate.update(updateWithVersionSql,
				new Object[] { 	data.getValue(), System.currentTimeMillis(),
								data.getVersion(), data.getNamespace(), data.getKey(),
								new Long(version) });

        // throwing cryptic 500 errors to the client absolutely sucks. They need to be
        // more informative, like: the version mismatched, fix your client.

		if (rowupdated != 1)  {
            // TODO: this needs to get back to client as intelligible error, not as internal 500 error
	   		throw new VersionMismatchException("row updated : " + rowupdated);
	   	}		
	}
	
	private void insertObject(KVObject data) throws IOException {
   		try {
			jdbcTemplate.update(insertSql,	   	
				new Object[] {	data.getNamespace(), data.getKey(), data.getClassName(),
	   							data.getValue(),
								data.getUpdateTime(),
								new Long(1)});
   		} catch (org.springframework.dao.DuplicateKeyException e) {
   			throw new DuplicationKeyException ("namespace: " + data.getNamespace() + " key:" + data.getKey() );
   		}
	}
	@Override
	public void create(final String namespace, final String key, String className, byte[] value) throws IOException {
		KVObject kvo = new KVObject(namespace, key, className, value);
		insertObject(kvo);
	}

	@Override
	public void put(KVObject data) throws IOException {
		try {
			insertObject(data);
		} catch (DuplicationKeyException e) {
			update(data);
		}
	}
}
