package com.migrate.webdata.model;

import java.io.Serializable;

/**
 * Envelope fields that enable object persistence in a webdata storage service.
 *
 * @author Zane Pan
 */
public interface PersistentObject extends Serializable {
	String getWd_namespace();
	void setWd_namespace(String type);
	
	String getWd_id();
	void setWd_id(String key);

	long getWd_version();
	void setWd_version(long version);

	long getWd_updateTime();
	void setWd_updateTime(long updateTime);
	
	String getWd_classname();
	void setWd_classname(String classname);

    public boolean isWd_deleted();
    public void setWd_isDeleted(boolean wd_deleted);
}
