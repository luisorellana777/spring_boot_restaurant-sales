package com.example.restaurant.restaurantsales.security.service;

public interface TokenService {

	public String getJWTToken(String username);
}
