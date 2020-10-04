package com.example.restaurant.restaurantsales.config;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class TestConfig {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Bean
	public MockMvc createMockMvc() {
		return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Bean
	public MediaType createMediaType() {
		return new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
				Charset.forName("utf8"));
	}
}
