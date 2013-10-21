package com.migrate.webdata.model;

/**
 * Zane Pan
 */
import java.util.HashMap;
import java.util.Map;

public class GenericMap extends HashMap<String, Object> implements PersistentObject {
    static private final String WD_NAMESPACE = "wd_namespace";

    // TODO: rename namespace back to bucket, and classname to namespace

    /**
     * If the generic map is a schema object, then this constant names the schema ID
     * field. If its a data object, then it names a UUID.
     */
    static private final String WD_ID = "wd_id";
    static private final String WD_VERSION = "wd_version";
    static private final String WD_UPDATE_TIME = "wd_updateTime";
    static private final String WD_CLASSNAME = "wd_classname";

    private static final long serialVersionUID = 7004975737945391947L;
    private Map<String, Object> map;


    @Override
    public String getWd_namespace() {
        return (String) super.get(WD_NAMESPACE);
    }

    @Override
    public void setWd_namespace(String value) {
        super.put(WD_NAMESPACE, value);
    }


    @Override
    public String getWd_id() {
        return (String) super.get(WD_ID);
    }

    @Override
    public void setWd_id(String value) {
        super.put(WD_ID, value);
    }

    @Override
    public long getWd_version() {
        return getLongValue(super.get(WD_VERSION));
    }

    @Override
    public void setWd_version(long version) {

        super.put(WD_VERSION, new Long(version));
    }

    @Override
    public long getWd_updateTime() {
        return getLongValue(super.get(WD_UPDATE_TIME));
    }

    @Override
    public void setWd_updateTime(long updateTime) {
        super.put(WD_UPDATE_TIME, new Long(updateTime));
    }

    @Override
    public String getWd_classname() {
        return (String) super.get(WD_CLASSNAME);
    }

    @Override
    public void setWd_classname(String classname) {
        super.put(WD_CLASSNAME, classname);
    }

    private long getLongValue(Object obj) {
        if ( obj instanceof Double ) {
            return ((Double) obj).longValue();
        } else if (obj instanceof Long) {
            return ((Long) obj).longValue();
        } else if (obj instanceof Integer) {
            // ZANE: hack, not sure what the client is going to send...
            return Long.valueOf((Integer)obj);
        }

        throw new IllegalArgumentException(" bad type :" + ((null != obj ? obj.getClass().getName(): "null")));
    }
}
