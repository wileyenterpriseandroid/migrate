package com.migrate.storage;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Zane Pan
 */


public class KVObject implements Serializable {
	public static final int ANY_VERSION = 0;
	private static final long serialVersionUID = 3352850855330969122L;
	private String bucket;
	private String className;
	private String id;
	private byte[] value;	
	private long version;
	private long updateTime;
	
	public KVObject() {}
	
	public KVObject(String bucket, String key, String className, byte[] value) {
		this.bucket = bucket;
		this.id = key;
		this.className = className;
		this.value = value;
		this.updateTime = System.currentTimeMillis();
		this.version = 0;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getKey() {
		return id;
	}

	public void setKey(String key) {
		this.id = key;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (updateTime ^ (updateTime >>> 32));
		result = prime * result + Arrays.hashCode(value);
		result = prime * result + (int) (version ^ (version >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KVObject other = (KVObject) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (updateTime != other.updateTime)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
}
