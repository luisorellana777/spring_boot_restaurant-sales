package com.example.restaurant.restaurantsales.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.example.restaurant.restaurantsales.repository.SalesRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableScheduling
public class SchedulerFallbackConfig {

	SalesRepository salesRepository;

	@Autowired
	public SchedulerFallbackConfig(@Lazy SalesRepository salesRepository) {
		this.salesRepository = salesRepository;
	}

	@Transactional
	@Scheduled(cron = "${restaurant-sales.attriblog-scheduler.cron-expression}")
	public void scheduleSaleFallback() {
		log.info("Servicio planificador -> Reintento de almacenado de venta no ingresadas en la cola.");
		List<SaleDto> salesDto = salesRepository.findAllSaleFallback();
		salesDto.forEach(saleDto -> {
			salesRepository.pushSale(saleDto);
			salesRepository.deleteSaleFallback(saleDto);

			log.info("Proceso Fallback -> Se han almacenado las siguientes ventas: {}", salesDto);
		});

	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
