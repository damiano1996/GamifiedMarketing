package com.derin.damiano.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The persistent class for the loginhistory database table.
 * 
 */
@Entity
@Table(name = "loginhistory", schema = "db_gamified_marketing")
@NamedQuery(name = "LoginHistory.findAll", query = "SELECT l FROM LoginHistory l")
public class LoginHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LoginHistoryPK id;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public LoginHistory() {
	}

	public LoginHistory(User user, Date date) {
		this.id = new LoginHistoryPK(user, date);
		this.user = user;
	}

	public LoginHistoryPK getId() {
		return this.id;
	}

	public void setId(LoginHistoryPK id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}