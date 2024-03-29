package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the usertable database table.
 * 
 */
@Entity
@Table(name = "usertable", schema = "db_gamified_marketing")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
@NamedQuery(name = "User.checkCredentials", query = "SELECT u FROM User u WHERE u.username = ?1 AND u.password = ?2")
@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = ?1")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private boolean admin;

	private boolean blocked;

	private String email;

	private String name;

	private String password;

	private String surname;

	private String username;

	// bi-directional many-to-one association to Review
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Review> reviews;

	// bi-directional many-to-one association to Answer
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Answer> answers;

	// bi-directional many-to-one association to LoginHistory
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<LoginHistory> loginHistories;

	// bi-directional many-to-one association to Statisticaldata
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Statisticaldata> statisticaldata;

	// bi-directional many-to-one association to GamificationPoint
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<GamificationPoint> gamificationPoints;

	// bi-directional many-to-one association to CancelledQuestionnaire
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<CancelledQuestionnaire> cancelledQuestionnaires;

	public User() {
	}

	public User(String username, String email, String password, String name, String surname) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isAdmin() {
		return this.admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isBlocked() {
		return this.blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setUser(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setUser(null);

		return review;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setUser(this);

		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setUser(null);

		return answer;
	}

	public List<LoginHistory> getLoginhistories() {
		return this.loginHistories;
	}

	public void setLoginhistories(List<LoginHistory> loginHistories) {
		this.loginHistories = loginHistories;
	}

	public LoginHistory addLoginhistory(LoginHistory loginHistory) {
		getLoginhistories().add(loginHistory);
		loginHistory.setUser(this);

		return loginHistory;
	}

	public LoginHistory removeLoginhistory(LoginHistory loginHistory) {
		getLoginhistories().remove(loginHistory);
		loginHistory.setUser(null);

		return loginHistory;
	}

	public List<Statisticaldata> getStatisticaldata() {
		return this.statisticaldata;
	}

	public void setStatisticaldata(List<Statisticaldata> statisticaldata) {
		this.statisticaldata = statisticaldata;
	}

	public Statisticaldata addStatisticaldata(Statisticaldata statisticaldata) {
		getStatisticaldata().add(statisticaldata);
		statisticaldata.setUser(this);

		return statisticaldata;
	}

	public Statisticaldata removeStatisticaldata(Statisticaldata statisticaldata) {
		getStatisticaldata().remove(statisticaldata);
		statisticaldata.setUser(null);

		return statisticaldata;
	}

	public List<GamificationPoint> getGamificationPoints() {
		return this.gamificationPoints;
	}

	public void setGamificationPoint(List<GamificationPoint> gamificationPoints) {
		this.gamificationPoints = gamificationPoints;
	}

	public GamificationPoint addGamificationPoint(GamificationPoint gamificationPoint) {
		getGamificationPoints().add(gamificationPoint);
		gamificationPoint.setUser(this);

		return gamificationPoint;
	}

	public GamificationPoint removeGamificationPoint(GamificationPoint gamificationPoint) {
		getGamificationPoints().remove(gamificationPoint);
		gamificationPoint.setUser(null);

		return gamificationPoint;
	}

	public List<CancelledQuestionnaire> getCancelledQuestionnaires() {
		return this.cancelledQuestionnaires;
	}

	public void setCancelledQuestionnaires(List<CancelledQuestionnaire> cancelledQuestionnaires) {
		this.cancelledQuestionnaires = cancelledQuestionnaires;
	}

	public CancelledQuestionnaire addCancelledQuestionnaire(CancelledQuestionnaire cancelledQuestionnaire) {
		getCancelledQuestionnaires().add(cancelledQuestionnaire);
		cancelledQuestionnaire.setUser(this);

		return cancelledQuestionnaire;
	}

}