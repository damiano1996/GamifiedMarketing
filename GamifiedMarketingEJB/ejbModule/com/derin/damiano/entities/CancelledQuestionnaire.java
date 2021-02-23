package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the cancelledquestionnaire database table.
 * 
 */
@Entity
@Table(name = "cancelledquestionnaire", schema = "db_gamified_marketing")
@NamedQuery(name = "CancelledQuestionnaire.findAll", query = "SELECT c FROM CancelledQuestionnaire c")
public class CancelledQuestionnaire implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public CancelledQuestionnaire() {
	}

	public CancelledQuestionnaire(Product product, User user) {
		this.product = product;
		this.user = user;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}