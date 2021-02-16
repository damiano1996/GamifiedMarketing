package com.derin.damiano.services;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.derin.damiano.entities.Answer;
import com.derin.damiano.entities.LoginHistory;
import com.derin.damiano.entities.Product;
import com.derin.damiano.entities.Question;
import com.derin.damiano.entities.Review;
import com.derin.damiano.entities.Statisticaldata;
import com.derin.damiano.entities.User;

/**
 * Session Bean implementation class ProductService
 */
@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
	public QuestionnaireService() {
		// TODO Auto-generated constructor stub
	}

	public void addQuestionnaire(User user, List<Answer> answers, int age, String sex, String expertiseLevel) {
		for (Answer answer : answers) {
			if (!answer.getContent().equals("")) {
				entityManager.persist(answer);
			}
		}
		entityManager.persist(
				new Statisticaldata(age, sex, expertiseLevel, answers.get(0).getQuestion().getProduct(), user));
	}

	public boolean isQuestionnaireSubmitted(User user, Product product) {
		for (Answer answer : user.getAnswers()) {
			if (answer.getQuestion().getProduct().getDate().equals(product.getDate()))
				return true;
		}
		return false;
	}

}
