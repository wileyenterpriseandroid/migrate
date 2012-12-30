package com.migrate.webdata;

public class MenuItem {
	private String name;
	private String description;
	private boolean enabled = true;
	private boolean special = false;
	private boolean newProduct = false;
	private boolean soldOut = false;
	private double unitPrice;
	private double orgUnitPrice;
	private int listPriority;
	private String optionId;
	private PropertyContainter propertyContainer ;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isSpecial() {
		return special;
	}
	public void setSpecial(boolean special) {
		this.special = special;
	}
	public boolean isNewProduct() {
		return newProduct;
	}
	public void setNewProduct(boolean newProduct) {
		this.newProduct = newProduct;
	}
	public boolean isSoldOut() {
		return soldOut;
	}
	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public double getOrgUnitPrice() {
		return orgUnitPrice;
	}
	public void setOrgUnitPrice(double orgUnitPrice) {
		this.orgUnitPrice = orgUnitPrice;
	}
	public int getListPriority() {
		return listPriority;
	}
	public void setListPriority(int listPriority) {
		this.listPriority = listPriority;
	}
	public String getOptionId() {
		return optionId;
	}
	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}
	public PropertyContainter getPropertyContainer() {
		return propertyContainer;
	}
	public void setPropertyContainer(PropertyContainter propertyContainer) {
		this.propertyContainer = propertyContainer;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + listPriority;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (newProduct ? 1231 : 1237);
		result = prime * result
				+ ((optionId == null) ? 0 : optionId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(orgUnitPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((propertyContainer == null) ? 0 : propertyContainer
						.hashCode());
		result = prime * result + (soldOut ? 1231 : 1237);
		result = prime * result + (special ? 1231 : 1237);
		temp = Double.doubleToLongBits(unitPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		MenuItem other = (MenuItem) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled != other.enabled)
			return false;
		if (listPriority != other.listPriority)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (newProduct != other.newProduct)
			return false;
		if (optionId == null) {
			if (other.optionId != null)
				return false;
		} else if (!optionId.equals(other.optionId))
			return false;
		if (Double.doubleToLongBits(orgUnitPrice) != Double
				.doubleToLongBits(other.orgUnitPrice))
			return false;
		if (propertyContainer == null) {
			if (other.propertyContainer != null)
				return false;
		} else if (!propertyContainer.equals(other.propertyContainer))
			return false;
		if (soldOut != other.soldOut)
			return false;
		if (special != other.special)
			return false;
		if (Double.doubleToLongBits(unitPrice) != Double
				.doubleToLongBits(other.unitPrice))
			return false;
		return true;
	}
	



}
