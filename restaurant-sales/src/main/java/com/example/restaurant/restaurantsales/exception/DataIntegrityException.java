package com.example.restaurant.restaurantsales.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataIntegrityException(String message) {
		super(message);
		log.error(message);
	}
}