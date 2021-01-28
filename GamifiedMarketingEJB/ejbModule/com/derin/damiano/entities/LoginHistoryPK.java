package com.derin.damiano.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The primary key class for the loginhistory database table.
 * 
 */
@Embeddable
public class LoginHistoryPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id", insertable = false, updatable = false)
	private int userId;

	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date date;

	public LoginHistoryPK() {
	}

	public LoginHistoryPK(User user, Date date) {
		this.userId = user.getId();
		this.date = date;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public java.util.Date getDate() {
		return this.date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LoginHistoryPK)) {
			return false;
		}
		LoginHistoryPK castOther = (LoginHistoryPK) other;
		return (this.userId == castOther.userId) && this.date.equals(castOther.date);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.date.hashCode();

		return hash;
	}
}