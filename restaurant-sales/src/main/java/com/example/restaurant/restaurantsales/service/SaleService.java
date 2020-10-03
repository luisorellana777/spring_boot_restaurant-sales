package com.example.restaurant.restaurantsales.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.restaurant.restaurantsales.dto.SaleDto;

public interface SaleService {

	public ResponseEntity<Object> pullSale();

	public ResponseEntity<Object> pullSales();

	public ResponseEntity<Object> pushSales(List<SaleDto> salesDto);

	public ResponseEntity<Object> pushSale(SaleDto saleDto);
}
