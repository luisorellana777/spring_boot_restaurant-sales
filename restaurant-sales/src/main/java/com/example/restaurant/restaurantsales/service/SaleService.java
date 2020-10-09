package com.example.restaurant.restaurantsales.service;

import java.util.List;

import com.example.restaurant.restaurantsales.dto.SaleDto;

public interface SaleService {

	public SaleDto pullSale();

	public List<SaleDto> pullSales();

	public String pushSales(List<SaleDto> salesDto);

	public String pushSale(SaleDto saleDto);
}
