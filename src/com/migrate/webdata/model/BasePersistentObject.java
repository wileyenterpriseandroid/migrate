package com.migrate.webdata.model;

/**
 * @author Zane Pan
 */
public class BasePersistentObject implements PersistentObject {
    public static final String WD_NAMESPACE_COLUMN = "wd_namespace";

    // schema id and data id are both wd_id
    public static final String WD_SCHEMA_ID_COLUMN = "wd_id";
    public static final String WD_TYPE_COLUMN = "wd_type";
    public static final String WD_VERSION_COLUMN = "wd_version";
    public static final String WD_NAME_NAME = "wd_name";
    public static final String WD_SCHEMA_UPDATE_TIME_COLUMN = "wd_schema_updateTime";
    public static final String WD_DELETED = "wd_deleted";

	private static final long serialVersionUID = 634871409470406478L;
	private String wd_id;
	private long wd_version;
	
	
	public boolean isWd_deleted() {
		return wd_deleted;
	}

	public void setWd_isDeleted(boolean wd_deleted) {
		this.wd_deleted = wd_deleted;
	}

	private String wd_classname;
	private long wd_updateTime;
	private String wd_namespace;
	private boolean wd_deleted;
	
	
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
