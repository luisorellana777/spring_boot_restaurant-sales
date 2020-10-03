package com.example.restaurant.restaurantsales.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ConfigurationValues {

	@Value("${restaurant-sales.values.neto}")
	public Float netoValue;

	@Value("${restaurant-sales.values.tax}")
	public Float taxValue;

	@Value("${rabbitmq.queue-name}")
	public String queueName;

	@Value("${rabbitmq.exchange-name}")
	private String exchangeName;

	@Value("${rabbitmq.host}")
	private String host;

	@Value("${rabbitmq.port}")
	private Integer port;

	@Value("${rabbitmq.user}")
	private String user;

	@Value("${rabbitmq.password}")
	private String password;

	@Value("${rabbitmq.virtualhost}")
	private String virtualhost;
}
