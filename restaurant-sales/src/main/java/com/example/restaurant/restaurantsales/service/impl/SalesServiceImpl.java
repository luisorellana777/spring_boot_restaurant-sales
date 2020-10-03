package com.example.restaurant.restaurantsales.service.impl;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.restaurant.restaurantsales.config.ConfigurationValues;
import com.example.restaurant.restaurantsales.dto.AmountDto;
import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.example.restaurant.restaurantsales.exception.DataIntegrityException;
import com.example.restaurant.restaurantsales.service.SaleService;
import com.example.restaurant.restaurantsales.util.SaleCalculator;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SalesServiceImpl implements SaleService {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	ConfigurationValues configurationValues;

	@Autowired
	SaleCalculator saleCalculator;

	@Override
	public ResponseEntity<Object> pullSales() {
		try {

			SaleDto receiveAndConvert = (SaleDto) rabbitTemplate.receiveAndConvert(configurationValues.getQueueName());
			log.info("{}", receiveAndConvert);
			return new ResponseEntity<>(receiveAndConvert, HttpStatus.OK);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<Object> pushSales(List<SaleDto> salesDto) {
		try {

			return null;
		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<Object> pushSale(SaleDto saleDto) {
		try {

			AmountDto calculatedAmounts = saleCalculator.calculateAmounts(saleDto);
			saleDto.setAmounts(calculatedAmounts);
			rabbitTemplate.convertAndSend(configurationValues.getQueueName(), saleDto);

			return new ResponseEntity<>("{ \"mensaje\" : \"Venta Almacenada en Cola\"", HttpStatus.ACCEPTED);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

}