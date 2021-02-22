package com.derin.damiano.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.derin.damiano.entities.LoginHistory;
import com.derin.damiano.entities.Product;
import com.derin.damiano.entities.Question;
import com.derin.damiano.entities.User;

/**
 * Session Bean implementation class ProductService
 */
@Stateless
public class ProductService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
	public ProductService() {
		// TODO Auto-generated constructor stub
	}

	public void createNewProduct(Date date, byte[] image, String name, String[] questions) {
		addProduct(date, image, name);
		addQuestions(date, questions);
	}

	public void addProduct(Date date, byte[] image, String name) {
		Product product = new Product(date, image, name);
		entityManager.persist(product);
		entityManager.flush();
	}

	public void addQuestions(Date date, String[] questions) {
		Product product = getProductByDate(date);

		for (String questionString : questions) {
			Question question = new Question(product, questionString);
			product.addQuestion(question);
			entityManager.persist(question);

		}

		entityManager.persist(product);
		entityManager.flush();
	}

	public Product getProductByDate(Date date) {
		Product product = null;
		try {
			product = entityManager.createNamedQuery("Product.productByDate", Product.class).setParameter(1, date)
					.getSingleResult();

		} catch (PersistenceException e) {
		}
		return product;
	}

	public Product getProductOfTheDay() {
		return getProductByDate(new Date());
	}
	
	public void deleteProduct(Date productDate) {
		Product product = entityManager.find(Product.class, productDate);
		entityManager.remove(product);
	}

}
