package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the answer database table.
 * 
 */
@Embeddable
public class AnswerPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "question_id", insertable = false, updatable = false)
	private int questionId;

	@Column(name = "id_creator", insertable = false, updatable = false)
	private int idCreator;

	public AnswerPK() {
	}

	public int getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
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
		if (!(other instanceof AnswerPK)) {
			return false;
		}
		AnswerPK castOther = (AnswerPK) other;
		return (this.questionId == castOther.questionId) && (this.idCreator == castOther.idCreator);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.questionId;
		hash = hash * prime + this.idCreator;

		return hash;
	}
}