package com.migrate.webdata.model;

import java.io.Serializable;
import java.util.List;

public class SyncResult implements Serializable {
	private static final long serialVersionUID = -932468247735639720L;
	private List<GenericMap> contactList;
	long synchTime;
	
	/* need this for JSON */
	public SyncResult() {}
	
	public SyncResult(List<GenericMap> list, long time) {
		this.contactList = list;
		this.synchTime = time;
	}
	public List<GenericMap> getGenericMapList() {
		return contactList;
	}
	public void setGenericMapList(List<GenericMap> contactList) {
		this.contactList = contactList;
	}
	public long getSynchTime() {
		return synchTime;
	}
	public void setSynchTime(long synchTime) {
		this.synchTime = synchTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contactList == null) ? 0 : contactList.hashCode());
		result = prime * result + (int) (synchTime ^ (synchTime >>> 32));
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
		SyncResult other = (SyncResult) obj;
		if (contactList == null) {
			if (other.contactList != null)
				return false;
		} else if (!contactList.equals(other.contactList))
			return false;
		if (synchTime != other.synchTime)
			return false;
		return true;
	}
}
	