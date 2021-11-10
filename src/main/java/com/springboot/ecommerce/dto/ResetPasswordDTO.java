package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {
	private String otp;
	@NotBlank(message = "Password must not be blank!")
	@Length(min = 6, message = "Password must be more than 6 characters!")
	private String password;

	@NotBlank(message = "Password must not be blank!")
	@Length(min = 6, message = "Password must be more than 6 characters!")
	private String confirmPassword;
}
