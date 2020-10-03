package com.example.restaurant.restaurantsales.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.restaurant.restaurantsales.config.ConfigurationValues;
import com.example.restaurant.restaurantsales.dto.AmountDto;
import com.example.restaurant.restaurantsales.dto.ProductDto;
import com.example.restaurant.restaurantsales.dto.SaleDto;

@Component
public class SaleCalculator {

	@Autowired
	ConfigurationValues configurationValues;

	public AmountDto calculateAmounts(SaleDto saleDto) {

		List<ProductDto> products = saleDto.getProducts();

		Long total = 0L;
		for (ProductDto productDto : products) {
			total = total + productDto.getPrice();
		}

		Long neto = (long) (total / configurationValues.getNetoValue());

		Long tax = (long) (configurationValues.getTaxValue() * neto);

		total = total + saleDto.getTip();
		return new AmountDto(neto, tax, total);
	}
}
