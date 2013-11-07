package com.migrate.storage;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Zane Pan
 */


public class KVObject implements Serializable {
	public static final int ANY_VERSION = 0;
	private static final long serialVersionUID = 3352850855330969122L;
	private String namespace;
	private String className;
	private String dataKey;
	private byte[] value;	
	private long version;
	private long updateTime;
    private String deleted;

    public KVObject() {}
	
	public KVObject(String namespace, String key, String className, byte[] value) {
		this.namespace = namespace;
		this.dataKey = key;
		this.className = className;
		this.value = value;
		this.updateTime = System.currentTimeMillis();
		this.version = 0;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
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
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((dataKey == null) ? 0 : dataKey.hashCode());
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
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (dataKey == null) {
			if (other.dataKey != null)
				return false;
		} else if (!dataKey.equals(other.dataKey))
			return false;
		if (updateTime != other.updateTime)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeleted() {
        return deleted;
    }
}
