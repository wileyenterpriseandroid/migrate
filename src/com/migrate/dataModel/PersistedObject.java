package com.migrate.dataModel;

/**
 * @author Zane Pan
 */

import java.io.Serializable;

public interface PersistedObject extends Serializable {
	String getBucket();
	void setBucket(String type);
	String getId();
	void setId(String key);
	long getVersion();
	void setVersion(long version);
	long getUpdateTime();
	void setUpdateTime(long updateTime);
}
