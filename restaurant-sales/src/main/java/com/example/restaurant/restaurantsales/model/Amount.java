package com.example.restaurant.restaurantsales.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Amount {

	@Id
	@GeneratedValue
	private Long id;

	private Long neto;

	private Long tax;

	private Long total;

	@OneToOne(cascade = CascadeType.ALL)
	private Sale sale;
}
