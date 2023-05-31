package org.java.spring.pojo;

import java.util.List;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Pizza {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToMany(mappedBy = "pizza")
	private List<SpecialOffer> specialOffers;
	
	@NotBlank
	@Size(min = 3 , max = 100)
	private String name;

	@Size(min = 10 , max = 255)
	private String description;
	
	@URL
	private String imgUrl;
	
	@NotNull
	@Min(1)
	private float price;
	
	private float discountedPrice;
	
	private boolean deleted = false;
	
	public Pizza(){}

	public Pizza(String name, String description, String imgUrl, float price) {
		setName(name);
		setDescription(description);
		setImgUrl(imgUrl);
		setPrice(price);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<SpecialOffer> getSpecialOffers() {
		return specialOffers;
	}

	public void setSpecialOffers(List<SpecialOffer> specialOffers) {
		this.specialOffers = specialOffers;
	}

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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public float getDiscountedPrice() {
		int maxDiscount = 0;
		
		for(SpecialOffer so : getSpecialOffers()) {
			maxDiscount += so.getDiscount();
		}
		
		float discountedPrice = getPrice() - (getPrice()/100 * maxDiscount);
		
		return discountedPrice;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	private String getInfo() {
		return "Id: " + getId()
			+ "\n" + "Name: " + getName()
			+ "\n" + "Description: " + getDescription()
			+ "\n" + "ImgUrl: " + getImgUrl()
			+ "\n" + "Price: " + getPrice() + "$";
	}
	
	@Override
	public String toString() {
		return getInfo();
	}
}
