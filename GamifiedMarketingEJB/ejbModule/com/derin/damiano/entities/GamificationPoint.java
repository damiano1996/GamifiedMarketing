package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the gamificationpoint database table.
 * 
 */
@Entity
@Table(name = "gamificationpoint", schema = "db_gamified_marketing")
@NamedQuery(name = "GamificationPoint.findAll", query = "SELECT g FROM GamificationPoint g")
@NamedQuery(name = "GamificationPoint.pointsByDay", query = "SELECT g FROM GamificationPoint g WHERE g.product.date = ?1 and g.user.id = ?2")
public class GamificationPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int points;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public GamificationPoint() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
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