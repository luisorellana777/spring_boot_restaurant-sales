package com.example.restaurant.restaurantsales.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@SQLDelete(sql = "update sale set is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
public class Sale {

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(mappedBy = "sale", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();

	@OneToOne(mappedBy = "sale", cascade = CascadeType.ALL)
	private Amount amounts;

	private Long tip;

	private boolean isDeleted;

}
