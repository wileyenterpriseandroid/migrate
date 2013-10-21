package com.migrate.rest;

import com.migrate.webdata.model.GenericMap;

import java.io.Serializable;
import java.util.List;

public class SyncResult implements Serializable {
    private static final long serialVersionUID = -4202285332791622654L;
    private List<GenericMap> modified;
    private List<GenericMap> conflicts;
    private long syncTime;

    public SyncResult() {

    }

    public List<GenericMap> getModified() {
        return modified;
    }

    public SyncResult(List<GenericMap> modified, List<GenericMap> conflicts, long syncTime) {
        super();
        this.modified = modified;
        this.conflicts = conflicts;
        this.syncTime = syncTime;
    }

    public void setModified(List<GenericMap> modified) {
        this.modified = modified;
    }

    public List<GenericMap> getConflicts() {
        return conflicts;
    }

    public void setConflicts(List<GenericMap> conflicts) {
        this.conflicts = conflicts;
    }

    public long getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((conflicts == null) ? 0 : conflicts.hashCode());
        result = prime * result
                + ((modified == null) ? 0 : modified.hashCode());
        result = prime * result + (int) (syncTime ^ (syncTime >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SyncResult other = (SyncResult) obj;
        if (conflicts == null) {
            if (other.conflicts != null) {
                return false;
            }
        } else if (!conflicts.equals(other.conflicts)) {
            return false;
        }
        if (modified == null) {
            if (other.modified != null) {
                return false;
            }
        } else if (!modified.equals(other.modified)) {
            return false;
        }
        if (syncTime != other.syncTime) {
            return false;
        }
        return true;
    }
}
