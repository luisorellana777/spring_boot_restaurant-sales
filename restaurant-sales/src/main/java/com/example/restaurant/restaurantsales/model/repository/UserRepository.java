package com.example.restaurant.restaurantsales.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restaurant.restaurantsales.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByName(String name);
}
