package com.example.restaurant.restaurantsales.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.restaurant.restaurantsales.config.ConfigurationValues;
import com.example.restaurant.restaurantsales.dto.AmountDto;
import com.example.restaurant.restaurantsales.dto.ProductDto;
import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SaleCalculatorUtil {

	@Autowired
	ConfigurationValues configurationValues;

	@Autowired
	@Qualifier("createObjectMapper")
	private ObjectMapper mapper;

	public AmountDto calculateAmounts(SaleDto saleDto) {

		List<ProductDto> products = saleDto.getProducts();

		Long total = 0L;
		for (ProductDto productDto : products) {
			total = total + productDto.getPrice();
		}

		Long neto = (long) (total / configurationValues.getNetoValue());

		Long tax = (long) (configurationValues.getTaxValue() * neto);

		total = total + saleDto.getWaiter().getTip();
		return new AmountDto(neto, tax, total);
	}

	public String getMessage(final String gotMessage) throws JsonProcessingException {

		@SuppressWarnings("unused")
		class MessageInnerClass {
			public String message = gotMessage;

		}

		return mapper.writeValueAsString(new MessageInnerClass());
	}

	public String getMessage(final String gotMessage, final String gotToken) throws JsonProcessingException {

		@SuppressWarnings("unused")
		class MessageInnerClass {
			public String message = gotMessage;
			public String token = gotToken;
		}

		return mapper.writeValueAsString(new MessageInnerClass());
	}
}
