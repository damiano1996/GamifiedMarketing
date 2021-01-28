package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the review database table.
 * 
 */
@Entity
@Table(name = "review", schema = "db_gamified_marketing")
@NamedQuery(name = "Review.findAll", query = "SELECT r FROM Review r")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ReviewPK id;

	@Lob
	private String content;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "id_creator")
	private User user;

	public Review() {
	}

	public Review(Product product, User author, String content) {
		this.id = new ReviewPK(product.getDate(), author.getId());
		this.content = content;
		this.product = product;
		this.user = author;
	}

	public ReviewPK getId() {
		return this.id;
	}

	public void setId(ReviewPK id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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