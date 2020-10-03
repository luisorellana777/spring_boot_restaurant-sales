package com.example.restaurant.restaurantsales.service;

import org.springframework.http.ResponseEntity;

public interface LoginService {

	public ResponseEntity<Object> login(String email, String password);
}
