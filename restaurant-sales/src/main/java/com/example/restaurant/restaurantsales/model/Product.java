package com.example.restaurant.restaurantsales.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "update product set is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
public class Product {

	@Id
	@GeneratedValue
	private Long id;

	private String sku;

	private Long price;

	@ManyToOne(cascade = CascadeType.ALL)
	private Sale sale;

	private boolean isDeleted;
}
