package com.example.restaurant.restaurantsales.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = ProductDto.class)
public class ProductDto {

	@NotBlank(message = "Se le debe asignar un sku")
	private String sku;
	@Min(value = 1, message = "El producto debe tener un precio valido")
	private Long price;
}
