package com.derin.damiano.services;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.derin.damiano.entities.LoginHistory;
import com.derin.damiano.entities.User;
import com.derin.damiano.exceptions.UsernameInUseException;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager entityManager;

	public UserService() {
	}

	public void addUser(String username, String email, String password, String name, String surname)
			throws UsernameInUseException {

		User user = null;
		try {

			user = entityManager.createNamedQuery("User.findByUsername", User.class).setParameter(1, username)
					.getSingleResult();
		} catch (Exception e) {
		}

		if (user == null)
			entityManager.persist(new User(username, email, password, name, surname));
		else
			throw new UsernameInUseException();
	}

	public User checkCredentials(String username, String password) {
		User user = null;
		try {
			user = entityManager.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username)
					.setParameter(2, password).getSingleResult();

			LoginHistory loginHistory = new LoginHistory(user, new Date());
			user.addLoginhistory(loginHistory);

			entityManager.persist(user);

		} catch (PersistenceException e) {
		}
		return user;
	}

}
