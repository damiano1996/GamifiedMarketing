package com.derin.damiano.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the question database table.
 * 
 */
@Entity
@Table(name = "question", schema = "db_gamified_marketing")
@NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q ORDER BY q.id ASC")
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Lob
	private String content;

	// bi-directional many-to-one association to Answer
	@OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
	private List<Answer> answers;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	public Question() {
	}

	public Question(Product product, String content) {
		this.product = product;
		this.content = content;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setQuestion(this);

		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setQuestion(null);

		return answer;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}