package com.example.restaurant.restaurantsales.security.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.example.restaurant.restaurantsales.security.service.TokenService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenServiceImpl implements TokenService {

	private static final String SECRET_KEY = "mySecretKey";
	private static final String AUTHORITY = "ROLE_USER";
	private static final String JWT_ID = "keyIdJWT";
	private static final String JWT_CLIMER = "authorities";
	private static final String PREFIX = "Bearer ";

	public String getJWTToken(String username) {
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(AUTHORITY);

		String token = Jwts.builder().setId(JWT_ID).setSubject(username)
				.claim(JWT_CLIMER,
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 300000))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()).compact();

		return PREFIX.concat(token);
	}

}