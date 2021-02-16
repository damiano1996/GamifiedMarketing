package com.derin.damiano.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The primary key class for the statisticaldata database table.
 * 
 */
@Embeddable
public class StatisticaldataPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "product_date", insertable = false, updatable = false)
	private java.util.Date productDate;

	@Column(name = "user_id", insertable = false, updatable = false)
	private int userId;

	public StatisticaldataPK() {
	}
	
	public StatisticaldataPK(Date productDate, int userId) {
		this.productDate = productDate;
		this.userId = userId;
	}

	public java.util.Date getProductDate() {
		return this.productDate;
	}

	public void setProductDate(java.util.Date productDate) {
		this.productDate = productDate;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof StatisticaldataPK)) {
			return false;
		}
		StatisticaldataPK castOther = (StatisticaldataPK) other;
		return this.productDate.equals(castOther.productDate) && (this.userId == castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productDate.hashCode();
		hash = hash * prime + this.userId;

		return hash;
	}
}