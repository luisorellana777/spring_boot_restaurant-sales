package com.example.restaurant.restaurantsales.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.example.restaurant.restaurantsales.service.SaleService;

@RestController
public class SaleController {

	@Autowired
	private SaleService saleService;

	@GetMapping("/sales")
	public ResponseEntity<Object> pullSales() {

		return saleService.pullSales();
	}

	@PostMapping("/sale")
	public ResponseEntity<Object> pushSale(@Valid @RequestBody SaleDto saleDto) {

		return saleService.pushSale(saleDto);
	}

	@PostMapping("/sales")
	public ResponseEntity<Object> pushSales(@Valid @RequestBody List<SaleDto> salesDto) {

		return saleService.pushSales(salesDto);
	}
}
