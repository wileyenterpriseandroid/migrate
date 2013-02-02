package com.migrate.webdata.model;

/**
 * @author Zane Pan
 */
public class BasePersistentObject implements PersistentObject {
	private static final long serialVersionUID = 634871409470406478L;
	private String wd_id;
	private long wd_version;
	private String wd_classname;
	private long wd_updateTime;
	private String wd_namespace;
	
	
	public BasePersistentObject() {
	}

	public String getWd_namespace() {
		return wd_namespace;
	}

	public void setWd_namespace(String wd_namespace) {
		this.wd_namespace = wd_namespace;
	}

	public String getWd_id() {
		return wd_id;
	}

	public void setWd_id(String wd_id) {
		this.wd_id = wd_id;
	}

	public long getWd_version() {
		return wd_version;
	}

	public void setWd_version(long wd_version) {
		this.wd_version = wd_version;
	}

	public String getWd_classname() {
		return wd_classname;
	}

	public void setWd_classname(String wd_classname) {
		this.wd_classname = wd_classname;
	}

	public long getWd_updateTime() {
		return wd_updateTime;
	}

	public void setWd_updateTime(long wd_updateTime) {
		this.wd_updateTime = wd_updateTime;
	}

	
}
