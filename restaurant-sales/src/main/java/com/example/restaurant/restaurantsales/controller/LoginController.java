package com.example.restaurant.restaurantsales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.restaurantsales.service.LoginService;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PutMapping(path = "/login")
	public ResponseEntity<Object> login(@RequestParam("email") String email,
			@RequestParam("password") String password) {

		String message = this.loginService.login(email, password);
		return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
	}
}
