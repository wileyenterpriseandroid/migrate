package com.migrate.webdata;

import com.migrate.webdata.model.BasePersistentObject;

import java.util.ArrayList;

/*
 * This class models a menu such as seafood menu 
 */
import java.util.List;

public class Menu extends BasePersistentObject {
	private static final long serialVersionUID = 3905132175109207406L;
	private List<MenuItem> menuItemList = new ArrayList<MenuItem>();
	public List<MenuItem> getMenuItemList() {
		return menuItemList;
	}
	public void setMenuItemList(List<MenuItem> menuItemList) {
		this.menuItemList = menuItemList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((menuItemList == null) ? 0 : menuItemList.hashCode());
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
		Menu other = (Menu) obj;
		if (menuItemList == null) {
			if (other.menuItemList != null)
				return false;
		} else if (!menuItemList.equals(other.menuItemList))
			return false;
		return true;
	}


	
}
