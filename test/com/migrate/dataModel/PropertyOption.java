package com.migrate.dataModel;

public class PropertyOption {
	private String name;
	private String description;
	private double price ;
	private double relativePrice;
	private boolean useRelativePrice = true;
	
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	public double getRelativePrice() {
		return relativePrice;
	}
	public void setRelativePrice(double relativePrice) {
		this.relativePrice = relativePrice;
	}
	public boolean isUseRelativePrice() {
		return useRelativePrice;
	}
	public void setUseRelativePrice(boolean useRelativePrice) {
		this.useRelativePrice = useRelativePrice;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(relativePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (useRelativePrice ? 1231 : 1237);
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
		PropertyOption other = (PropertyOption) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (Double.doubleToLongBits(relativePrice) != Double
				.doubleToLongBits(other.relativePrice))
			return false;
		if (useRelativePrice != other.useRelativePrice)
			return false;
		return true;
	}
	
	
}
