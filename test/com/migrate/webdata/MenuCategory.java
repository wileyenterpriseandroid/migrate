package com.migrate.webdata;

import com.migrate.webdata.model.BasePersistentObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * This class models a menu category such as Lunch menu etc.
 */

public class MenuCategory extends BasePersistentObject {
	private static final long serialVersionUID = 2274690568099676712L;
	private ServTimeRange[] servTimeRages = new ServTimeRange[7];
	private List<Menu> menuList = new ArrayList<Menu>();
	private List<String> menuRefList = new ArrayList<String>();
	private boolean bEmbedded = true;
	
	public MenuCategory() {
		
	}
	public MenuCategory(String bucket, String key, String name) {
		//super(bucket, key, name);
	}
	
	public MenuCategory(String bucket, String key, String name, boolean bEmbedded) {
		this(bucket, key, name);
		this.bEmbedded = bEmbedded;
	}
	

	public ServTimeRange[] getServTimeRages() {
		return servTimeRages;
	}


	public void setServTimeRages(ServTimeRange[] servTimeRages) {
		this.servTimeRages = servTimeRages;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}


	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}


	public List<String> getMenuRefList() {
		return menuRefList;
	}


	public void setMenuRefList(List<String> menuRefList) {
		this.menuRefList = menuRefList;
	}


	public boolean isbEmbedded() {
		return bEmbedded;
	}


	public void setbEmbedded(boolean bEmbedded) {
		this.bEmbedded = bEmbedded;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (bEmbedded ? 1231 : 1237);
		result = prime * result
				+ ((menuList == null) ? 0 : menuList.hashCode());
		result = prime * result
				+ ((menuRefList == null) ? 0 : menuRefList.hashCode());
		result = prime * result + Arrays.hashCode(servTimeRages);
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
		MenuCategory other = (MenuCategory) obj;
		if (bEmbedded != other.bEmbedded)
			return false;
		if (menuList == null) {
			if (other.menuList != null)
				return false;
		} else if (!menuList.equals(other.menuList))
			return false;
		if (menuRefList == null) {
			if (other.menuRefList != null)
				return false;
		} else if (!menuRefList.equals(other.menuRefList))
			return false;
		if (!Arrays.equals(servTimeRages, other.servTimeRages))
			return false;
		return true;
	}


	public static class ServTimeRange {
		private int startHour;
		private int startMin;
		private int endHour;
		private int endMin;		
		
		public ServTimeRange(int startH, int startMin, int endH, int endMin){
			this.startHour = startH;
			this.startMin = startMin;
			this.endHour = endH;
			this.endMin = endMin;
		}
		
		public int getStartHour() {
			return startHour;
		}
		public void setStartHour(int startHour) {
			this.startHour = startHour;
		}
		public int getStartMin() {
			return startMin;
		}
		public void setStartMin(int startMin) {
			this.startMin = startMin;
		}
		public int getEndHour() {
			return endHour;
		}
		public void setEndHour(int endHour) {
			this.endHour = endHour;
		}
		public int getEndMin() {
			return endMin;
		}
		public void setEndMin(int endMin) {
			this.endMin = endMin;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + endHour;
			result = prime * result + endMin;
			result = prime * result + startHour;
			result = prime * result + startMin;
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
			ServTimeRange other = (ServTimeRange) obj;
			if (endHour != other.endHour)
				return false;
			if (endMin != other.endMin)
				return false;
			if (startHour != other.startHour)
				return false;
			if (startMin != other.startMin)
				return false;
			return true;
		}
		
	}
}
