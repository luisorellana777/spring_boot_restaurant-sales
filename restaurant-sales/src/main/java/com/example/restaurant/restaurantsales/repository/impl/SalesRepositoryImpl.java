package com.example.restaurant.restaurantsales.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.restaurant.restaurantsales.config.ConfigurationValues;
import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.example.restaurant.restaurantsales.model.Sale;
import com.example.restaurant.restaurantsales.model.repository.SaleRepository;
import com.example.restaurant.restaurantsales.repository.SalesRepository;

@Repository
public class SalesRepositoryImpl implements SalesRepository {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	ConfigurationValues configurationValues;

	@Autowired
	SaleRepository saleRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public void pushSale(SaleDto saleDto) {
		rabbitTemplate.convertAndSend(configurationValues.getQueueName(), saleDto);
	}

	@Override
	public SaleDto pullSale() {
		SaleDto receiveAndConvert = (SaleDto) rabbitTemplate.receiveAndConvert(configurationValues.getQueueName());
		return receiveAndConvert;
	}

	@Override
	public void saveSalesFallback(List<SaleDto> salesDto) {

		List<Sale> salesEntity = new ArrayList<Sale>();

		salesDto.forEach(saleDto -> {
			Sale saleEntity = modelMapper.map(saleDto, Sale.class);
			saleEntity.getAmounts().setSale(saleEntity);
			saleEntity.getProducts().parallelStream().forEach(product -> {
				product.setSale(saleEntity);
			});

			salesEntity.add(saleEntity);
		});
		saleRepository.saveAll(salesEntity);
		saleRepository.flush();
	}

	@Override
	public List<SaleDto> findAllSaleFallback() {

		List<SaleDto> salesDto = new ArrayList<SaleDto>();
		List<Sale> salesEntity = saleRepository.findAll();
		salesEntity.parallelStream().forEach(saleEntity -> {

			SaleDto saleDto = modelMapper.map(saleEntity, SaleDto.class);
			salesDto.add(saleDto);
		});

		return salesDto;
	}

	@Override
	public void deleteSaleFallback(SaleDto saleDto) {

		Sale saleEntity = modelMapper.map(saleDto, Sale.class);
		saleEntity.getAmounts().setSale(saleEntity);
		saleEntity.getProducts().parallelStream().forEach(product -> {
			product.setSale(saleEntity);
		});

		saleRepository.deleteById(saleEntity.getId());
	}

}
