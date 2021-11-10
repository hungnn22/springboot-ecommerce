package com.springboot.ecommerce.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
	private Long id;

	@NotBlank(message = "Name must not be blank!")
	private String name;

	@Email(message = "Email is invalid!")
	@NotBlank(message = "Email is invalid!")
	private String email;

	private String avatar;

	@Override
	public String toString() {
		return "Profile [id=" + id + ", name=" + name + ", email=" + email + ", avatar=" + avatar + "]";
	}

}
