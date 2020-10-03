package com.example.restaurant.restaurantsales.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = AmountDto.class)
public class AmountDto {

	@Min(value = 0, message = "El valor neto debe ser valido")
	@Max(value = 0, message = "El valor neto debe ser valido")
	private Long neto;
	@Min(value = 0, message = "El valor del iva debe ser valido")
	@Max(value = 0, message = "El valor del iva debe ser valido")
	private Long tax;
	@Min(value = 0, message = "El valor total debe ser valido")
	@Max(value = 0, message = "El valor total debe ser valido")
	private Long total;
}
