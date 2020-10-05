package com.example.restaurant.restaurantsales.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = WaiterDto.class)
public class WaiterDto {

	@Min(value = 0, message = "El valor de la propina debe ser valido")
	private Long tip;

	@Pattern(regexp = "^0*(\\d{1,3}(\\.?\\d{3})*)\\-?([\\dkK])$", message = "El rut debe ser valido: Ejemplo (00012348-k)")
	private String rut;

	@NotBlank(message = "Se debe especificar un nombre")
	private String fisrtName;

	@NotBlank(message = "Se debe especificar un apellido")
	private String lastName;
}
