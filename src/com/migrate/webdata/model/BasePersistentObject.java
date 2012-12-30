package com.migrate.webdata.model;

/**
 * @author Zane Pan
 */
public abstract class BasePersistentObject implements PersistentObject {
	private static final long serialVersionUID = 634871409470406478L;
	private String bucket;
	private String key;
	private long version;
	private String name;
	private String description;
	private long updateTime;
	
	public BasePersistentObject() {
	}

    public BasePersistentObject(String bucket, String key, String name) {
		this(bucket, key, name, 0);
	}

	public BasePersistentObject(String bucket, String key, String name, long version) {
		this.bucket = bucket;
		this.key = key;
		this.name = name;
		this.version = version;
	}	

    public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getId() {
		return key;
	}

	public void setId(String key) {
		this.key = key;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (updateTime ^ (updateTime >>> 32));
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
		BasePersistentObject other = (BasePersistentObject) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (updateTime != other.updateTime)
			return false;
		return true;
	}
}
