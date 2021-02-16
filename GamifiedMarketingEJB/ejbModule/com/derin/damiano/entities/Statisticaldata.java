package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the statisticaldata database table.
 * 
 */
@Entity
@Table(name = "statisticaldata", schema = "db_gamified_marketing")
@NamedQuery(name = "Statisticaldata.findAll", query = "SELECT s FROM Statisticaldata s")
public class Statisticaldata implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private StatisticaldataPK id;

	private int age;

	@Column(name = "expertise_level")
	private String expertiseLevel;

	private String sex;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Statisticaldata() {
	}

	public Statisticaldata(int age, String sex, String expertiseLevel, Product product, User user) {
		this.id = new StatisticaldataPK(product.getDate(), user.getId());
		this.age = age;
		this.expertiseLevel = expertiseLevel;
		this.sex = sex;
		this.product = product;
		this.user = user;
	}

	public StatisticaldataPK getId() {
		return this.id;
	}

	public void setId(StatisticaldataPK id) {
		this.id = id;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getExpertiseLevel() {
		return this.expertiseLevel;
	}

	public void setExpertiseLevel(String expertiseLevel) {
		this.expertiseLevel = expertiseLevel;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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