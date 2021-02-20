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
import com.derin.damiano.entities.Review;
import com.derin.damiano.entities.User;

/**
 * Session Bean implementation class ProductService
 */
@Stateless
public class ReviewService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
	public ReviewService() {
		// TODO Auto-generated constructor stub
	}

	public void addReview(Date productDate, int userId, String content) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		Review review = new Review(product, user, content);
		user.addReview(review);
		product.addReview(review);

		// entityManager.persist(review);
		entityManager.persist(user);
		entityManager.persist(product);
		entityManager.flush();
	}

	public boolean isReviewSubmitted(Date productDate, int userId) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		for (Review review : user.getReviews()) {
			if (review.getProduct().getDate().equals(product.getDate()))
				return true;
		}
		return false;
	}

}
