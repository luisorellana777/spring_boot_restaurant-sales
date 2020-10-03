package com.example.restaurant.restaurantsales;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restaurant.restaurantsales.config.ConfigurationValues;
import com.example.restaurant.restaurantsales.dto.AmountDto;
import com.example.restaurant.restaurantsales.dto.ProductDto;
import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SalesMockTest {

	@Autowired
	private ConfigurationValues configurationValues;

	@Autowired
	private AmqpAdmin amqpAdmin;

	@Autowired
	@Qualifier("createMockMvc")
	private MockMvc mockMvc;

	@Autowired
	@Qualifier("createObjectMapper")
	private ObjectMapper mapper;

	@Autowired
	@Qualifier("createMediaType")
	private MediaType mediaType;

	@Test
	@Order(3)
	public void singleCreationAndValidation() throws Exception {

		SaleDto saleDto = new SaleDto();
		ProductDto productoDto = new ProductDto("SKU", 1000L);

		saleDto.setTip(0L);
		saleDto.addPproduct(productoDto);

		mockMvc.perform(post("/sale/").contentType(mediaType).content(mapper.writeValueAsString(saleDto)))
				.andExpect(status().isAccepted());

		AmountDto value = new AmountDto(889L, 119L, 1000L);
		saleDto.setAmounts(value);

		mockMvc.perform(post("/sale/").contentType(mediaType).content(mapper.writeValueAsString(saleDto)))
				.andExpect(status().isBadRequest());

	}

	@Test
	@Order(4)
	public void multipleCreationAndValidation() throws Exception {

		List<SaleDto> sales = new ArrayList<>();
		for (int i = 1; i < 101; i++) {

			SaleDto saleDto = new SaleDto();
			ProductDto productoDto = new ProductDto("SKU", (long) (Math.random() * 1000 * i));
			saleDto.setTip((long) (productoDto.getPrice() / (productoDto.getPrice() + i)));
			saleDto.addPproduct(productoDto);

			sales.add(saleDto);
		}

		mockMvc.perform(post("/sales/").contentType(mediaType).content(mapper.writeValueAsString(sales)))
				.andExpect(status().isAccepted());

		sales.parallelStream().forEach(sale -> {

			AmountDto value = new AmountDto(889L, 119L, 1000L);
			sale.setAmounts(value);
		});

		mockMvc.perform(post("/sale/").contentType(mediaType).content(mapper.writeValueAsString(sales)))
				.andExpect(status().isBadRequest());

	}

	@Test
	@Order(1)
	public void singleCreationAndReturnAndValidation() throws Exception {

		SaleDto saleDto = new SaleDto();
		ProductDto productoDto = new ProductDto("SKU", 1000L);

		saleDto.setTip(0L);
		saleDto.addPproduct(productoDto);

		mockMvc.perform(post("/sale/").contentType(mediaType).content(mapper.writeValueAsString(saleDto)))
				.andExpect(status().isAccepted());

		mockMvc.perform(get("/sale/")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.products[0].sku").value("SKU"));

	}

	@Test
	@Order(2)
	public void multipleCreationAndReturnAndValidation() throws Exception {

		List<SaleDto> sales = new ArrayList<>();
		for (int i = 1; i < 101; i++) {

			SaleDto saleDto = new SaleDto();
			ProductDto productoDto = new ProductDto("SKU", (long) (Math.random() * 1000 * i));
			saleDto.setTip((long) (productoDto.getPrice() / (productoDto.getPrice() + i)));
			saleDto.addPproduct(productoDto);

			sales.add(saleDto);
		}

		mockMvc.perform(post("/sales/").contentType(mediaType).content(mapper.writeValueAsString(sales)))
				.andExpect(status().isAccepted());

		mockMvc.perform(get("/sales/")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(100));

	}

	@AfterAll
	public void cleanUp() {

		amqpAdmin.deleteQueue(configurationValues.getQueueName());
	}

	@BeforeEach
	public void purgateUp() {

		amqpAdmin.purgeQueue(configurationValues.getQueueName());
	}

}
