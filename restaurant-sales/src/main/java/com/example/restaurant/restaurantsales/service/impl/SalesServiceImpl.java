package com.example.restaurant.restaurantsales.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.example.restaurant.restaurantsales.exception.DataIntegrityException;
import com.example.restaurant.restaurantsales.exception.RabbitConnectionException;
import com.example.restaurant.restaurantsales.repository.SalesRepository;
import com.example.restaurant.restaurantsales.service.SaleService;
import com.example.restaurant.restaurantsales.util.SaleCalculatorUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SalesServiceImpl implements SaleService {

	@Autowired
	SalesRepository salesRepository;

	@Autowired
	SaleCalculatorUtil calculatorUtil;

	@Override
	public ResponseEntity<Object> pullSales() {
		try {

			List<SaleDto> salesDto = new ArrayList<SaleDto>();
			while (true) {

				SaleDto receiveAndConvert = salesRepository.pullSale();

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

			//TODO: El metodo del repository no se deberia llamar igual a este metdo... No se entiende porque no es claro.
			SaleDto receiveAndConvert = salesRepository.pullSale();
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

			salesDto.parallelStream().forEach(saleDto -> saleDto.setAmounts(calculatorUtil.calculateAmounts(saleDto)));
			salesDto.forEach(saleDto -> salesRepository.pushSale(saleDto));

			return new ResponseEntity<>(calculatorUtil.getMessage("Ventas Almacenadas en Cola."), HttpStatus.ACCEPTED);

		} catch (AmqpException amqEx) {

			throw new RabbitConnectionException(salesDto);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<Object> pushSale(SaleDto saleDto) {
		try {

			saleDto.setAmounts(calculatorUtil.calculateAmounts(saleDto));
			salesRepository.pushSale(saleDto);

			return new ResponseEntity<>(calculatorUtil.getMessage("Venta Almacenada en Cola."), HttpStatus.ACCEPTED);

		} catch (AmqpException amqEx) {

			throw new RabbitConnectionException(saleDto);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

}