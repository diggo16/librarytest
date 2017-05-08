package com.cybercom.librarytest.rest;

import javax.persistence.EntityManager;

import com.cybercom.librarytest.LocalEntityManagerFactory;
import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Loan;
import com.cybercom.librarytest.model.User;

/**
 * Base class for the REST services of the Library test application.
 * @author lemor1
 *
 */
public class LibraryRestService {
	protected static boolean doesUserExistInDatabase(Long id) {
		if (id == null) {
			return false;
		}
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		boolean exists = em.find(User.class, id) != null;
		em.close();
		return exists;
	}
	
	protected static boolean doesBookExistInDatabase(Long id) {
		if (id == null) {
			return false;
		}
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		boolean exists = em.find(Book.class, id) != null;
		em.close();
		return exists;
	}
	
	protected static boolean doesLoanExistInDatabase(Long id) {
		if (id == null) {
			return false;
		}
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		boolean exists = em.find(Loan.class, id) != null;
		em.close();
		return exists;
	}
	
	protected static boolean doesAuthorExistInDatabase(Long id) {
		if (id == null) {
			return false;
		}
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		boolean exists = em.find(Author.class, id) != null;
		em.close();
		return exists;
	}
}
