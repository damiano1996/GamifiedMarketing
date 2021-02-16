package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@Table(name = "answer", schema = "db_gamified_marketing")
@NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AnswerPK id;

	@Lob
	private String content;

	// bi-directional many-to-one association to Question
	@ManyToOne
	private Question question;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "id_creator")
	private User user;

	public Answer() {
	}

	public Answer(String content, Question question, User user) {
		this.id = new AnswerPK(question.getId(), user.getId());
		this.content = content;
		this.question = question;
		this.user = user;
	}

	public AnswerPK getId() {
		return this.id;
	}

	public void setId(AnswerPK id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}