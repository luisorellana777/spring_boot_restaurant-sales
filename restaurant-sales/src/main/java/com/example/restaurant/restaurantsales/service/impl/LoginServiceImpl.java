package com.example.restaurant.restaurantsales.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restaurant.restaurantsales.exception.UserNotFoundException;
import com.example.restaurant.restaurantsales.model.User;
import com.example.restaurant.restaurantsales.model.repository.UserRepository;
import com.example.restaurant.restaurantsales.security.service.TokenService;
import com.example.restaurant.restaurantsales.service.LoginService;
import com.example.restaurant.restaurantsales.util.SaleCalculatorUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LoginServiceImpl implements LoginService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private SaleCalculatorUtil saleCalculatorUtil;

	@Override
	public String login(String email, String password) {

		List<User> user = userRepository.findByName(email);
		if (user.isEmpty()) {
			throw new UserNotFoundException("Usuario(Email) o Clave No Encontrado");
		}

		String passwordEncoded = user.iterator().next().getPassword();

		if (passwordEncoder.matches(password, passwordEncoded)) {

			String token = tokenService.getJWTToken(email);
			log.info("Credencial Correcto. Token -> {}", token);

			return saleCalculatorUtil.getMessage("Credencial Correcto.", token);
		} else {

			throw new UserNotFoundException("Usuario(Email) o Clave No Encontrado");
		}

	}

}
