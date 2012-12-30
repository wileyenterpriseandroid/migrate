package com.migrate.webdata;

import com.migrate.webdata.model.BasePersistentObject;

import java.util.ArrayList;
import java.util.List;

public class MenuCategoryContainer extends BasePersistentObject {
	private static final long serialVersionUID = -2753939187577143782L;
	private List<MenuCategory> menuCategoryList = new ArrayList<MenuCategory>();
	
	
	public List<MenuCategory> getMenuCategoryList() {
		return menuCategoryList;
	}

	public void setMenuCategoryList(List<MenuCategory> menuCategoryList) {
		this.menuCategoryList = menuCategoryList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((menuCategoryList == null) ? 0 : menuCategoryList.hashCode());
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
		MenuCategoryContainer other = (MenuCategoryContainer) obj;
		if (menuCategoryList == null) {
			if (other.menuCategoryList != null)
				return false;
		} else if (!menuCategoryList.equals(other.menuCategoryList))
			return false;
		return true;
	}
	

}
