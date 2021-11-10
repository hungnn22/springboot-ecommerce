package com.springboot.ecommerce.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

	private Long id;

	@NotBlank(message = "Name must not be blank!")
	private String name;

	@Email(message = "Email is invalid!")
	@NotBlank(message = "Email is invalid!")
	private String email;

	@NotBlank(message = "Password must not be blank!")
	@Length(min = 6, message = "Password must be more than 6 characters!")
	private String password;

	@NotBlank(message = "Password must not be blank!")
	@Length(min = 6, message = "Password must be more than 6 characters!")
	private String confirmPassword;

	private String avatar;
}
