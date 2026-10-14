package app.rental.projet_3.dto;

import java.math.BigDecimal;

public class RentalRequestDto {

	private String name;

	private BigDecimal surface;

	private BigDecimal price;

	private String picture;

	private String description;

	private Integer ownerId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSurface() {
		return surface;
	}

	public void setSurface(BigDecimal surface) {
		this.surface = surface;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
}
