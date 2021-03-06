package com.example.restaurant.restaurantsales.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, scope = SaleDto.class)
public class SaleDto {

	@JsonIgnore
	private Long id;
	@NotNull(message = "Se debe asignar al menos un producto")
	@NotEmpty(message = "Se debe asignar al menos un producto")
	@Valid
	private List<ProductDto> products = new ArrayList<>();
	@Null(message = "No se deben ingresar valores en la propiedad 'amounts'. Estos son auto-calculados")
	@Valid
	private AmountDto amounts;

	@NotNull(message = "Se debe asignar a un camarero")
	@Valid
	private WaiterDto waiter;

	@JsonIgnore
	public void addPproduct(ProductDto dto) {
		products.add(dto);
	}

}
