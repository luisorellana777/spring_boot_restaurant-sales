package com.example.restaurant.restaurantsales.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restaurant.restaurantsales.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}
