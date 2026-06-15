package in.sg.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pizzas")
public class Pizza {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pizzaId;
	
	@Column(nullable = false)
	private String pizzaName;
	
	@Column(nullable = false)
	private Double price;
	
	@Column(nullable = false)
	private String category;
	
	@Column(columnDefinition = "LONGTEXT")
	private String imageUrl;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private Integer availability = 1;
	
	public Pizza() {
		super();
	}

	public Pizza(String pizzaName, Double price, String category, String imageUrl, String description) {
		this.pizzaName = pizzaName;
		this.price = price;
		this.category = category;
		this.imageUrl = imageUrl;
		this.description = description;
	}

	public Long getPizzaId() {
		return pizzaId;
	}

	public void setPizzaId(Long pizzaId) {
		this.pizzaId = pizzaId;
	}

	public String getPizzaName() {
		return pizzaName;
	}

	public void setPizzaName(String pizzaName) {
		this.pizzaName = pizzaName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}

	@Override
	public String toString() {
		return "Pizza [pizzaId=" + pizzaId + ", pizzaName=" + pizzaName + ", price=" + price + ", category="
				+ category + "]";
	}
}
