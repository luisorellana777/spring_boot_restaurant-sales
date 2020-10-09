package com.example.restaurant.restaurantsales.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
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
	public List<SaleDto> pullSales() {
		try {

			List<SaleDto> salesDto = new ArrayList<SaleDto>();
			while (true) {

				SaleDto receiveAndConvert = salesRepository.pullSale();

				if (Objects.isNull(receiveAndConvert))
					break;

				salesDto.add(receiveAndConvert);
				log.info("{}", receiveAndConvert);
			}

			return salesDto;

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	public SaleDto pullSale() {
		try {

			SaleDto receiveAndConvert = salesRepository.pullSale();
			log.info("{}", receiveAndConvert);
			return receiveAndConvert;

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	@Transactional
	public String pushSales(List<SaleDto> salesDto) {
		try {

			salesDto.parallelStream().forEach(saleDto -> saleDto.setAmounts(calculatorUtil.calculateAmounts(saleDto)));
			salesDto.forEach(saleDto -> salesRepository.pushSale(saleDto));

			return calculatorUtil.getMessage("Ventas Almacenadas en Cola.");

		} catch (AmqpException amqEx) {

			throw new RabbitConnectionException(salesDto);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

	@Override
	public String pushSale(SaleDto saleDto) {
		try {

			saleDto.setAmounts(calculatorUtil.calculateAmounts(saleDto));
			salesRepository.pushSale(saleDto);

			return calculatorUtil.getMessage("Venta Almacenada en Cola.");

		} catch (AmqpException amqEx) {

			throw new RabbitConnectionException(saleDto);

		} catch (Exception ex) {

			throw new DataIntegrityException(ex.getMessage());
		}
	}

}