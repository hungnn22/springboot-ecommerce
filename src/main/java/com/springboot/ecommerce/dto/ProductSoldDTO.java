package com.springboot.ecommerce.dto;

import com.springboot.ecommerce.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSoldDTO {
	private Product product;
	private Double avg;
	private Double total;
}
