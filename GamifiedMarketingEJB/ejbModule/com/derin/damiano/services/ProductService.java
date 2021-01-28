package com.derin.damiano.services;

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

	public void addProduct(Date date, byte[] image, String name, String[] questions) {
		Product product = new Product(date, image, name);
		entityManager.persist(product);

		for (String question : questions) {
			entityManager.persist(new Question(product, question));
		}
	}

	public Product getProductOfTheDay() {
		Product product = null;
		try {
			product = entityManager.createNamedQuery("Product.productByDate", Product.class)
					.setParameter(1, new Date()).getSingleResult();

		} catch (PersistenceException e) {
		}
		return product;
	}

}
