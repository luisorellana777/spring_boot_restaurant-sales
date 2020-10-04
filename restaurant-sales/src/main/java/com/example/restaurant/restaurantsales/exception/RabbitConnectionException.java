package com.example.restaurant.restaurantsales.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.AmqpException;

import com.example.restaurant.restaurantsales.dto.SaleDto;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class RabbitConnectionException extends AmqpException {

	private static final long serialVersionUID = 1L;

	private final static String MESSAGE = "Error al intentar conectarse a RabbitMQ";
	private List<SaleDto> salesDto;

	public RabbitConnectionException(List<SaleDto> salesDto) {
		super(MESSAGE);
		this.salesDto = salesDto;
		this.logMessages();
	}

	public RabbitConnectionException(SaleDto saleDto) {
		super(MESSAGE);
		this.salesDto = new ArrayList<SaleDto>();
		this.salesDto.add(saleDto);
		this.logMessages();
	}

	private void logMessages() {

		log.error("Error al almacenar las siguientes ventas: {}", this.salesDto);
		log.info("Se activa proceso fallback para persistir venta");
	}
}