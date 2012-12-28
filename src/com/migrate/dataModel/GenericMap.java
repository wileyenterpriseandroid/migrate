package com.migrate.dataModel;

/**
 * Zane Pan
 */
import java.util.HashMap;
import java.util.Map;


public class GenericMap extends HashMap<String, Object> implements PersistedObject  {
	static private final String TYPE = "type" ;
	static private final String KEY = "key" ;
	static private final String VERSION = "version" ;
	static private final String UPDATETIME = "updateTime" ;
	private static final long serialVersionUID = 7004975737945391947L;
	private Map<String, Object> map;

	@Override
	public String getBucket() {
		return super.get(TYPE).toString();
	}
	@Override
	public void setBucket(String bucket) {
		super.put(TYPE, bucket);		
	}
	
	@Override
	public String getId() {
		return super.get(KEY).toString();
	}
	@Override
	public void setId(String key) {
		super.put(KEY, key);
	}
	@Override
	public long getVersion() {
		Object o = super.get(VERSION);
		if (o instanceof Integer) {
			return ((Integer)o).longValue();
		}
		return ((Long)o).longValue();
	}
	@Override
	public void setVersion(long version) {
		super.put(VERSION, new Long(version));
		
	}
	@Override
	public long getUpdateTime() {
		return ((Long)super.get(UPDATETIME)).longValue();

	}
	@Override
	public void setUpdateTime(long updateTime) {
		super.put(UPDATETIME, new Long(updateTime));
	}
}
