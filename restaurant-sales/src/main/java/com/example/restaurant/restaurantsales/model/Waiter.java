package com.example.restaurant.restaurantsales.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "update waiter set is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
public class Waiter {

	@Id
	@GeneratedValue
	private Long id;

	private Long tip;

	private String rut;

	private String fisrtName;

	private String lastName;

	@OneToOne(cascade = CascadeType.ALL)
	private Sale sale;

	private boolean isDeleted;
}
