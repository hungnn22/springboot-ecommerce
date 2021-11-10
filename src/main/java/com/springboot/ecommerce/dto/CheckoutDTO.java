package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDTO {
	@Override
	public String toString() {
		return "Checkout [address=" + address + ", phone=" + phone + ", note=" + note + "]";
	}

	@NotBlank(message = "Adress must not be blank!")
	private String address;

	@NotBlank(message = "Phone must not be blank!")
	private String phone;

	private String note;
}
