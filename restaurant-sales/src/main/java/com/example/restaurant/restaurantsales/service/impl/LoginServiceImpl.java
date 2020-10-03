package com.example.restaurant.restaurantsales.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restaurant.restaurantsales.exception.DataIntegrityException;
import com.example.restaurant.restaurantsales.exception.UserNotFoundException;
import com.example.restaurant.restaurantsales.model.User;
import com.example.restaurant.restaurantsales.model.repository.UserRepository;
import com.example.restaurant.restaurantsales.security.service.TokenService;
import com.example.restaurant.restaurantsales.service.LoginService;

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
	
	@Override
	public ResponseEntity<Object> login(String email, String password) {

		try {
			
			List<User> user = userRepository.findByName(email);
			if(user.isEmpty()) {throw new UserNotFoundException("Usuario(Email) o Clave No Encontrado");}
			
			String passwordEncoded = user.get(0).getPassword();
			
			if(passwordEncoder.matches(password, passwordEncoded)) {
				
				String token = tokenService.getJWTToken(email);
				log.info("Usuario correcto. Token -> {}", token);
		        return new ResponseEntity<>(
			  	          "{ \"mensaje\" : \"Login\","
			  	          + " \"token\" : \""+token+"\"}",
			  	          HttpStatus.ACCEPTED);
			} else {
				
				throw new UserNotFoundException("Usuario(Email) o Clave No Encontrado");	
			}
			
		} catch(Exception ex) {
			
			throw new DataIntegrityException(ex.getMessage());
		}

	}



}
