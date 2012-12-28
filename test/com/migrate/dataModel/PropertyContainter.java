package com.migrate.dataModel;

import java.util.ArrayList;
import java.util.List;

import com.migrate.dataModel.BasePersistedObject;

public class PropertyContainter extends BasePersistedObject {
	private static final long serialVersionUID = 4892854749155167902L;
	
	private List<Property> propertyList = new ArrayList<Property>();

	public List<Property> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<Property> propertyList) {
		this.propertyList = propertyList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((propertyList == null) ? 0 : propertyList.hashCode());
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
		PropertyContainter other = (PropertyContainter) obj;
		if (propertyList == null) {
			if (other.propertyList != null)
				return false;
		} else if (!propertyList.equals(other.propertyList))
			return false;
		return true;
	}
	

}
