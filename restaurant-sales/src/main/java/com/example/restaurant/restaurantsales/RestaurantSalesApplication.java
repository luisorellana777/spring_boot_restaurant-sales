package com.example.restaurant.restaurantsales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RestaurantSalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantSalesApplication.class, args);
	}

}
