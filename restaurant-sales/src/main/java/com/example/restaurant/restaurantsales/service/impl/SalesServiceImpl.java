package com.example.restaurant.restaurantsales.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant.restaurantsales.config.ConfigurationValues;
import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.example.restaurant.restaurantsales.exception.DataIntegrityException;
import com.example.restaurant.restaurantsales.service.SaleService;
import com.example.restaurant.restaurantsales.util.SaleCalculatorUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SalesServiceImpl implements SaleService {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	ConfigurationValues configurationValues;

	@Autowired
	SaleCalculatorUtil saleCalculatorUtil;

	@Override
	public ResponseEntity<Object> pullSales() {
		try {

			List<SaleDto> salesDto = new ArrayList<SaleDto>();
			while (true) {

				SaleDto receiveAndConvert = (SaleDto) rabbitTemplate
						.receiveAndConvert(configurationValues.getQueueName());

				if (Objects.isNull(receiveAndConvert))
					break;

				salesDto.add(receiveAndConvert);
				log.info("{}", receiveAndConvert);

			}

			return new ResponseEntity<>(salesDto, HttpStatus.OK);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<Object> pullSale() {
		try {

			SaleDto receiveAndConvert = (SaleDto) rabbitTemplate.receiveAndConvert(configurationValues.getQueueName());
			log.info("{}", receiveAndConvert);
			return new ResponseEntity<>(receiveAndConvert, HttpStatus.OK);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Object> pushSales(List<SaleDto> salesDto) {
		try {

			salesDto.forEach(saleDto -> {

				saleDto.setAmounts(saleCalculatorUtil.calculateAmounts(saleDto));
				rabbitTemplate.convertAndSend(configurationValues.getQueueName(), saleDto);
			});

			return new ResponseEntity<>(saleCalculatorUtil.getMessage("Ventas Almacenadas en Cola."),
					HttpStatus.ACCEPTED);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<Object> pushSale(SaleDto saleDto) {
		try {

			saleDto.setAmounts(saleCalculatorUtil.calculateAmounts(saleDto));
			rabbitTemplate.convertAndSend(configurationValues.getQueueName(), saleDto);

			return new ResponseEntity<>(saleCalculatorUtil.getMessage("Venta Almacenada en Cola."),
					HttpStatus.ACCEPTED);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

}