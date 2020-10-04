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

	@Value("${restaurant-sales.rabbitmq.queue-name}")
	public String queueName;

	@Value("${restaurant-sales.rabbitmq.exchange-name}")
	private String exchangeName;

	@Value("${restaurant-sales.rabbitmq.host}")
	private String host;

	@Value("${restaurant-sales.rabbitmq.port}")
	private Integer port;

	@Value("${restaurant-sales.rabbitmq.user}")
	private String user;

	@Value("${restaurant-sales.rabbitmq.password}")
	private String password;

	@Value("${restaurant-sales.rabbitmq.virtualhost}")
	private String virtualhost;
}
