package com.springboot.ecommerce.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + ", des=" + des + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@javax.validation.constraints.Size(min = 4, message = "Name must be longer than 4 characters")
	@NotBlank(message = "Name must not be empty!")
	private String name;

	@Min(value = 0, message = "Price must than 0!")
	@NotNull(message = "Price must not be empty!")
	private Double price;

	@Min(value = 0, message = "Quantity must than 0!")
	@NotNull(message = "Quantity must not be empty!")
	private Integer qty;

	private String des;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt = new Date();

	@OneToMany(mappedBy = "product")
	private List<Review> reviews = new ArrayList<Review>();

	@OneToMany(mappedBy = "product")
	private List<Photo> photos = new ArrayList<Photo>();

	@OneToMany(mappedBy = "product")
	private List<OrderDetail> details = new ArrayList<OrderDetail>();

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "color_id")
	private Color color;

	@ManyToOne
	@JoinColumn(name = "size_id")
	private Size size;

}
