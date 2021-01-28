package com.derin.damiano.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The primary key class for the review database table.
 * 
 */
@Embeddable
public class ReviewPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "product_date", insertable = false, updatable = false)
	private java.util.Date productDate;

	@Column(name = "id_creator", insertable = false, updatable = false)
	private int idCreator;

	public ReviewPK() {
	}

	public ReviewPK(Date productDate, int idCreator) {
		this.productDate = productDate;
		this.idCreator = idCreator;
	}

	public java.util.Date getProductDate() {
		return this.productDate;
	}

	public void setProductDate(java.util.Date productDate) {
		this.productDate = productDate;
	}

	public int getIdCreator() {
		return this.idCreator;
	}

	public void setIdCreator(int idCreator) {
		this.idCreator = idCreator;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ReviewPK)) {
			return false;
		}
		ReviewPK castOther = (ReviewPK) other;
		return this.productDate.equals(castOther.productDate) && (this.idCreator == castOther.idCreator);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productDate.hashCode();
		hash = hash * prime + this.idCreator;

		return hash;
	}
}