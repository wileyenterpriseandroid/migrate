package com.migrate.webdata.model;

import java.io.Serializable;

/**
 * Envelope fields that enable object persistence in a webdata storage service.
 *
 * @author Zane Pan
 */
public interface PersistentObject extends Serializable {
	String getBucket();
	void setBucket(String type);

	String getId();
	void setId(String key);

	long getVersion();
	void setVersion(long version);

	long getUpdateTime();
	void setUpdateTime(long updateTime);
}
