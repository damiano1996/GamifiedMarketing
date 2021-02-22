package com.derin.damiano.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

	public HashMap<User, Integer> getLeaderboard(Date productDate) {

		// To refresh all after triggers updated the points
		entityManager.flush();
		entityManager.getEntityManagerFactory().getCache().evictAll();

		Product product = entityManager.find(Product.class, productDate);

		List<Statisticaldata> statisticaldatas = product.getStatisticaldata();
		List<User> usersWhoSubmitted = new ArrayList<>();

		for (Statisticaldata statisticaldata : statisticaldatas) {
			usersWhoSubmitted.add(statisticaldata.getUser());
		}

		HashMap<User, Integer> leaderboard = new HashMap<User, Integer>();

		for (User user : usersWhoSubmitted) {
			Integer userPoints = getPointsByDay(productDate, user.getId());
			leaderboard.put(user, userPoints);
		}

		leaderboard = sortLeaderboard(leaderboard);

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

	private HashMap<User, Integer> sortLeaderboard(HashMap<User, Integer> leaderboard) {
		// Create a list from elements of HashMap
		List<Map.Entry<User, Integer>> list = new LinkedList<Map.Entry<User, Integer>>(leaderboard.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<User, Integer>>() {
			public int compare(Map.Entry<User, Integer> o1, Map.Entry<User, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<User, Integer> temp = new LinkedHashMap<User, Integer>();
		for (Map.Entry<User, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

}
