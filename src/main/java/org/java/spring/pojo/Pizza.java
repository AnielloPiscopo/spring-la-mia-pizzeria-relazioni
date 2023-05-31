package org.java.spring.pojo;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Pizza {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
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
			+ "\n" + "Price: " + getPrice();
	}
	
	@Override
	public String toString() {
		return getInfo();
	}
}
