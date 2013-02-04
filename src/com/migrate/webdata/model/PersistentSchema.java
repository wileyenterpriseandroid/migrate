package com.migrate.webdata.model;

import java.util.List;
import java.util.Map;

public class PersistentSchema extends BasePersistentObject {
	private static final long serialVersionUID = -7495813459656381108L;

    public static final String SCHEMA_TYPE_NAME = "type";
    public static final String PROPERTIES_NAME = "properties";
    public static final String PROPERTY_TYPE_NAME = "type";
    public static final String WD_RESERVED_PREFIX = "wd_";

    private Map<String, Object> jsonSchema;
	private List<PropertyIndex> indexList;

	 public enum DataType {
         String, Boolean, Number, Integer
     }

    public Map<String, Object> getJsonSchema() {
		return jsonSchema;
	}

	public void setJsonSchema(Map<String, Object> jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

	public List<PropertyIndex> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<PropertyIndex> indexList) {
		this.indexList = indexList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((indexList == null) ? 0 : indexList.hashCode());
		result = prime * result
				+ ((jsonSchema == null) ? 0 : jsonSchema.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistentSchema other = (PersistentSchema) obj;
		if (indexList == null) {
			if (other.indexList != null)
				return false;
		} else if (!indexList.equals(other.indexList))
			return false;
		if (jsonSchema == null) {
			if (other.jsonSchema != null)
				return false;
		} else if (!jsonSchema.equals(other.jsonSchema))
			return false;
		return true;
	}
}

