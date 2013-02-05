package com.migrate.webdata.model;
/**
 * Zane Pan
 */
import java.util.HashMap;
import java.util.Map;

public class GenericMap extends HashMap<String, Object> implements PersistentObject {
	static private final String NAMESPACE = "wd_namespace" ;
	static private final String ID = "wd_id" ;
	static private final String VERSION = "wd_version" ;
	static private final String UPDATETIME = "wd_updateTime" ;
	static private final String CLASSNAME = "wd_classname" ;
	
	private static final long serialVersionUID = 7004975737945391947L;
	private Map<String, Object> map;


	@Override
	public String getWd_namespace() {
		return (String) super.get(NAMESPACE);
	}

	@Override
	public void setWd_namespace(String value) {
		super.put(NAMESPACE, value);
	}
	
	
	@Override
	public String getWd_id() {
		return (String) super.get(ID);
	}

	@Override
	public void setWd_id(String value) {
		super.put(ID, value);
	}

	@Override
	public long getWd_version() {
		return getLongValue(super.get(VERSION));
	}

	@Override
	public void setWd_version(long version) {
		
		super.put(VERSION, new Long(version));
	}

	@Override
	public long getWd_updateTime() {
		return getLongValue(super.get(UPDATETIME));
	}

	@Override
	public void setWd_updateTime(long updateTime) {
		super.put(UPDATETIME, new Long(updateTime));
	}

	@Override
	public String getWd_classname() {
		return (String) super.get(CLASSNAME);
	}

	@Override
	public void setWd_classname(String classname) {
		super.put(CLASSNAME, classname);
	}

	private long getLongValue(Object obj) {
		if ( obj instanceof Double ) {
			return ((Double) obj).longValue();
		} else if (obj instanceof Long) {
			return ((Long) obj).longValue();
		}
		throw new IllegalArgumentException(" bad type :" + obj.getClass().getName());
	}
}
