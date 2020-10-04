package com.example.restaurant.restaurantsales.repository;

import java.util.List;

import com.example.restaurant.restaurantsales.dto.SaleDto;

public interface SalesRepository {

	public void pushSale(SaleDto saleDto);

	public SaleDto pullSale();

	public void saveSalesFallback(List<SaleDto> saleDto);

	public List<SaleDto> findAllSaleFallback();

	public void deleteSaleFallback(SaleDto saleDto);
}
