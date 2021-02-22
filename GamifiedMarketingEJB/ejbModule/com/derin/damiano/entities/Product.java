package com.derin.damiano.entities;

import java.util.Base64;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@Table(name = "product", schema = "db_gamified_marketing")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p ORDER BY p.date DESC")
@NamedQuery(name = "Product.productByDate", query = "SELECT p FROM Product p WHERE p.date = ?1")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Temporal(TemporalType.DATE)
	private Date date;

	@Lob
	private byte[] image;

	private String name;

	// bi-directional many-to-one association to Review
	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	private List<Review> reviews;

	// bi-directional many-to-one association to Question
	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	private List<Question> questions;

	// bi-directional many-to-one association to Statisticaldata
	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	private List<Statisticaldata> statisticaldata;

	// bi-directional many-to-one association to GamificationPoint
	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	private List<GamificationPoint> gamificationPoints;

	// bi-directional many-to-one association to CancelledQuestionnaire
	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	private List<CancelledQuestionnaire> cancelledQuestionnaires;

	public Product() {
	}

	public Product(Date date, byte[] image, String name) {
		this.date = date;
		this.image = image;
		this.name = name;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public byte[] getImage() {
		return this.image;
	}

	public String getImageData() {
		return Base64.getMimeEncoder().encodeToString(this.image);
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setProduct(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setProduct(null);

		return review;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<CancelledQuestionnaire> getCancelledQuestionnaires() {
		return this.cancelledQuestionnaires;
	}

	public void setCancelledQuestionnaires(List<CancelledQuestionnaire> cancelledQuestionnaires) {
		this.cancelledQuestionnaires = cancelledQuestionnaires;
	}

	public Question addQuestion(Question question) {
		getQuestions().add(question);
		question.setProduct(this);

		return question;
	}

	public Question removeQuestion(Question question) {
		getQuestions().remove(question);
		question.setProduct(null);

		return question;
	}

	public List<Statisticaldata> getStatisticaldata() {
		return this.statisticaldata;
	}

	public void setStatisticaldata(List<Statisticaldata> statisticaldata) {
		this.statisticaldata = statisticaldata;
	}

	public Statisticaldata addStatisticaldata(Statisticaldata statisticaldata) {
		getStatisticaldata().add(statisticaldata);
		statisticaldata.setProduct(this);

		return statisticaldata;
	}

	public Statisticaldata removeStatisticaldata(Statisticaldata statisticaldata) {
		getStatisticaldata().remove(statisticaldata);
		statisticaldata.setProduct(null);

		return statisticaldata;
	}

	public CancelledQuestionnaire addCancelledQuestionnaire(CancelledQuestionnaire cancelledQuestionnaire) {
		getCancelledQuestionnaires().add(cancelledQuestionnaire);
		cancelledQuestionnaire.setProduct(this);

		return cancelledQuestionnaire;
	}

}