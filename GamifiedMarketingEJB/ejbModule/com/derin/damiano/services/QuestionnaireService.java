package com.derin.damiano.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.derin.damiano.entities.Answer;
import com.derin.damiano.entities.Badword;
import com.derin.damiano.entities.LoginHistory;
import com.derin.damiano.entities.Product;
import com.derin.damiano.entities.Question;
import com.derin.damiano.entities.Review;
import com.derin.damiano.entities.Statisticaldata;
import com.derin.damiano.entities.User;
import com.derin.damiano.exceptions.BadwordException;
import com.derin.damiano.exceptions.BlockedException;
import com.derin.damiano.exceptions.EmptyQuestionnaireException;

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

	public void addQuestionnaire(Date productDate, int userId, List<Answer> answers, int age, String sex,
			String expertiseLevel) throws BlockedException, EmptyQuestionnaireException {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		try {

			if (!user.isBlocked()) { // just to be sure

				addAnswers(userId, answers);

				addStatisticaldata(productDate, userId, age, sex, expertiseLevel);

				entityManager.flush();

			} else {
				throw new BlockedException("User has been already blocked.");
			}

		} catch (BadwordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			// time to block the user
			user.setBlocked(true);
			entityManager.persist(user);
			entityManager.flush();

			throw new BlockedException("User has been blocked!");

		}
	}

	public void addAnswers(int userId, List<Answer> answers) throws BadwordException, EmptyQuestionnaireException {
		User user = entityManager.find(User.class, userId);

		for (Answer answer : answers) {
			if (!answer.getContent().equals("")) {

				// checking if it contains bad words
				isBadword(answer);

				Question question = entityManager.find(Question.class, answer.getId().getQuestionId());
				System.out.println("Question info: " + question.getId() + " - " + question.getContent());
				System.out.println("Answer info: " + answer.getId().getQuestionId() + " - " + answer.getContent());

				question.addAnswer(answer);
				user.addAnswer(answer);

				entityManager.persist(question);
				entityManager.persist(user);

			} else {
				throw new EmptyQuestionnaireException();
			}
		}

		entityManager.persist(user);
	}

	public void addStatisticaldata(Date productDate, int userId, int age, String sex, String expertiseLevel) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		Statisticaldata statisticaldata = new Statisticaldata(age, sex, expertiseLevel, product, user);
		user.addStatisticaldata(statisticaldata);
		product.addStatisticaldata(statisticaldata);

		entityManager.persist(user);
		entityManager.persist(product);

	}

	public ArrayList<Answer> getEmptyAnswers(Date productDate, int userId) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		ArrayList<Answer> answers = new ArrayList<Answer>();
		for (Question question : product.getQuestions()) {

			System.out.println("Question info: " + question.getId() + " - " + question.getContent());
			Answer answer = new Answer("", question, user);

//			user.addAnswer(answer);
//			question.addAnswer(answer);

			answers.add(answer);
		}
		return answers;
	}

	public boolean isQuestionnaireSubmitted(Date productDate, int userId) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		for (Answer answer : user.getAnswers()) {
			if (answer.getQuestion().getProduct().getDate().equals(product.getDate()))
				return true;
		}
		return false;
	}

	public ArrayList<User> getUsersWhoSubmitted(Date productDate) {
		List<User> users = null;
		try {
			users = entityManager.createNamedQuery("User.findAll", User.class).getResultList();
		} catch (PersistenceException e) {
		}

		ArrayList<User> whoSubmitted = new ArrayList<>();
		for (User user : users) {
			if (isQuestionnaireSubmitted(productDate, user.getId())) {
				whoSubmitted.add(user);
			}
		}
		return whoSubmitted;
	}

	public ArrayList<User> getUserWhoCancelledQuestionnaire(Date productDate) {
		ArrayList<User> whoCancelled = new ArrayList<>();
		return whoCancelled;
	}

	public ArrayList<Answer> getMarketingAnswers(Date productDate, int userId) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		ArrayList<Answer> answers = new ArrayList<>();

		for (Answer answer : user.getAnswers()) {
			if (answer.getQuestion().getProduct().getDate().equals(product.getDate())) {
				answers.add(answer);
			}
		}
		return answers;
	}

	public Statisticaldata getStatisticalAnswers(Date productDate, int userId) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		for (Statisticaldata statisticaldata : user.getStatisticaldata()) {
			if (statisticaldata.getProduct().getDate().equals(product.getDate())) {
				return statisticaldata;
			}
		}
		return null;
	}

	public ArrayList<Date> getAvailableQuestionnaires() {
		List<Product> products = null;
		try {
			products = entityManager.createNamedQuery("Product.findAll", Product.class).getResultList();

		} catch (PersistenceException e) {
		}

		ArrayList<Date> dates = new ArrayList<>();
		for (Product product : products) {
			dates.add(product.getDate());
		}
		return dates;
	}

	private void isBadword(Answer answer) throws BadwordException {

		System.out.println("Answer under investigation: " + answer.getContent());

		List<Badword> badwords = entityManager.createNamedQuery("Badword.findAll", Badword.class).getResultList();
		for (Badword badword : badwords) {
			System.out.println("Checking bad word: " + badword.getWord());

			if (answer.getContent().contains(badword.getWord()))
				throw new BadwordException("Bad word detected: " + badword.getWord());
		}
	}

}
