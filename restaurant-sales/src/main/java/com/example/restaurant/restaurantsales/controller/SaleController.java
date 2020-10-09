package com.example.restaurant.restaurantsales.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@GetMapping("/sale")
	public ResponseEntity<Object> pullSale() {

		SaleDto saleDto = saleService.pullSale();
		return new ResponseEntity<>(saleDto, HttpStatus.OK);
	}

	@GetMapping("/sales")
	public ResponseEntity<Object> pullSales() {

		List<SaleDto> saleDto = saleService.pullSales();
		return new ResponseEntity<>(saleDto, HttpStatus.OK);
	}

	@PostMapping("/sale")
	public ResponseEntity<Object> pushSale(@Valid @RequestBody SaleDto saleDto) {

		String message = saleService.pushSale(saleDto);
		return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
	}

	@PostMapping("/sales")
	public ResponseEntity<Object> pushSales(@Valid @RequestBody List<SaleDto> salesDto) {

		String message = saleService.pushSales(salesDto);
		return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
	}
}
