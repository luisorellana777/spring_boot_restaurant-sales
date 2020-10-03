package com.example.restaurant.restaurantsales;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.restaurant.restaurantsales.config.ConfigurationValues;
import com.example.restaurant.restaurantsales.dto.AmountDto;
import com.example.restaurant.restaurantsales.dto.ProductDto;
import com.example.restaurant.restaurantsales.dto.SaleDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SalesMockTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private ConfigurationValues configurationValues;

	@Autowired
	private AmqpAdmin amqpAdmin;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Test
	@Order(3)
	public void singleCreationAndValidation() throws Exception {

		SaleDto saleDto = new SaleDto();
		ProductDto productoDto = new ProductDto("SKU", 1000L);

		saleDto.setTip(0L);
		saleDto.addPproduct(productoDto);

		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(post("/sale/").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(saleDto)))
				.andExpect(status().isAccepted());

		AmountDto value = new AmountDto(889L, 119L, 1000L);
		saleDto.setAmounts(value);

		mockMvc.perform(post("/sale/").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(saleDto)))
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

		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(post("/sales/").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(sales)))
				.andExpect(status().isAccepted());

		sales.parallelStream().forEach(sale -> {

			AmountDto value = new AmountDto(889L, 119L, 1000L);
			sale.setAmounts(value);
		});

		mockMvc.perform(post("/sale/").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(sales)))
				.andExpect(status().isBadRequest());

	}

	@Test
	@Order(1)
	public void singleCreationAndReturnAndValidation() throws Exception {

		SaleDto saleDto = new SaleDto();
		ProductDto productoDto = new ProductDto("SKU", 1000L);

		saleDto.setTip(0L);
		saleDto.addPproduct(productoDto);

		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(post("/sale/").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(saleDto)))
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

		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(post("/sales/").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(sales)))
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

	@BeforeAll
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
}
