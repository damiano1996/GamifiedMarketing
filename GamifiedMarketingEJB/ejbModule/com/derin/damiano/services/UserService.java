package com.derin.damiano.services;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;


import com.derin.damiano.entities.LoginHistory;
import com.derin.damiano.entities.User;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager entityManager;

	public UserService() {
	}

	public void addUser(String username, String email, String password, String name, String surname) {
		entityManager.persist(new User(username, email, password, name, surname));
	}

	public User checkCredentials(String username, String password) {
		User user = null;
		try {
			user = entityManager.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username)
					.setParameter(2, password).getSingleResult();
			
			entityManager.persist(new LoginHistory(user, new Date()));

		} catch (PersistenceException e) {
		}
		return user;
	}

	public void updateProfile(User user) {
		try {
			entityManager.merge(user);
		} catch (PersistenceException e) {
		}
	}
}
