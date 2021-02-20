package com.derin.damiano.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.derin.damiano.entities.Answer;
import com.derin.damiano.entities.Badword;
import com.derin.damiano.entities.GamificationPoint;
import com.derin.damiano.entities.LoginHistory;
import com.derin.damiano.entities.Product;
import com.derin.damiano.entities.Question;
import com.derin.damiano.entities.Review;
import com.derin.damiano.entities.Statisticaldata;
import com.derin.damiano.entities.User;
import com.derin.damiano.exceptions.BadwordException;
import com.derin.damiano.exceptions.BlockedException;

/**
 * Session Bean implementation class ProductService
 */
@Stateless
public class LeaderboardService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
	public LeaderboardService() {
		// TODO Auto-generated constructor stub
	}

	public Map<Integer, User> getLeaderboard(Date productDate) {

		entityManager.flush();
		entityManager.getEntityManagerFactory().getCache().evictAll();

		List<User> users = null;
		try {
			users = entityManager.createNamedQuery("User.findAll", User.class).getResultList();

		} catch (PersistenceException e) {
		}

		Map<Integer, User> leaderboard = new TreeMap<Integer, User>(Collections.reverseOrder());

		for (User user : users) {
			Integer userPoints = getPointsByDay(productDate, user.getId());
			if (userPoints > 0)
				leaderboard.put(userPoints, user);
		}

		return leaderboard;
	}

	public int getPointsByDay(Date productDate, int userId) {
		Product product = entityManager.find(Product.class, productDate);
		User user = entityManager.find(User.class, userId);

		List<GamificationPoint> gamificationPoints = null;
		try {
			gamificationPoints = entityManager
					.createNamedQuery("GamificationPoint.pointsByDay", GamificationPoint.class)
					.setParameter(1, productDate).setParameter(2, userId).getResultList();

		} catch (PersistenceException e) {
		}

		int points = 0;
		for (GamificationPoint gamificationPoint : gamificationPoints) {
			points += gamificationPoint.getPoints();
		}
		return points;
	}

}
