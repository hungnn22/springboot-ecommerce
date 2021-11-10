package com.springboot.ecommerce.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Email must not be blank!")
	@Email(message = "Invalid email!")
	private String email;

	@NotBlank(message = "Password must not be blank!")
	@Size(min = 6, message = "Password must than 6 character!")
	private String password;

	@NotBlank(message = "Name must not be blank!")
	private String name;

	private Integer role = 0;

	private String avatar = "https://icon-library.com/images/no-user-image-icon/no-user-image-icon-0.jpg";

	private Integer status = 1;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt = new Date();

	@OneToMany(mappedBy = "account")
	private List<Review> reviews = new ArrayList<Review>();

	@OneToMany(mappedBy = "account")
	private List<Order> orders = new ArrayList<Order>();

	@Override
	public String toString() {
		return "Account [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", role=" + role
				+ ", avatar=" + avatar + ", status=" + status + ", createAt=" + createAt + "]";
	}

}
