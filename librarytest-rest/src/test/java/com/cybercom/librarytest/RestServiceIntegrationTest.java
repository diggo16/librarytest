package com.cybercom.librarytest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.junit.After;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Authors;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Books;
import com.cybercom.librarytest.model.Loan;
import com.cybercom.librarytest.model.Loans;
import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.model.Users; 

/**
 * Base class for integration tests.
 * @author Lennart Moraeus
 */
public abstract class RestServiceIntegrationTest {

	protected static final String TEST_AUTHOR_FIRST_NAME_1 = "Author first name 1";
	protected static final String TEST_AUTHOR_LAST_NAME_1 = "Author last name 1";
	protected static final String TEST_AUTHOR_COUNTRY_1 = "Author country 1";
	protected static final String TEST_AUTHOR_BIO_1 = "Author bio 1";
	protected static final String TEST_AUTHOR_FIRST_NAME_2 = "Author first name 2";
	protected static final String TEST_AUTHOR_LAST_NAME_2 = "Author last name 2";
	protected static final String TEST_AUTHOR_COUNTRY_2 = "Author country 2";
	protected static final String TEST_AUTHOR_BIO_2 = "Author bio 2";

	protected static final String TEST_USER_DISPLAY_NAME_1 = "User display name 1";
	protected static final User.Role TEST_USER_ROLE_1 = User.Role.LOANER;
	protected static final String TEST_USER_PASSWORD_1 = "User password 1";
	protected static final String TEST_USER_FIRST_NAME_1 = "User first name 1";
	protected static final String TEST_USER_LAST_NAME_1 = "User last name 1";
	protected static final String TEST_USER_PHONE_1 = "User phone 1";
	protected static final String TEST_USER_EMAIL_1 = "User email 1";
	protected static final String TEST_USER_DISPLAY_NAME_2 = "User display name 2";
	protected static final User.Role TEST_USER_ROLE_2 = User.Role.LIBRARIAN;
	protected static final String TEST_USER_PASSWORD_2 = "User password 2";
	protected static final String TEST_USER_FIRST_NAME_2 = "User first name 2";
	protected static final String TEST_USER_LAST_NAME_2 = "User last name 2";
	protected static final String TEST_USER_PHONE_2 = "User phone 2";
	protected static final String TEST_USER_EMAIL_2 = "User email 2";
	
	protected static final String TEST_BOOK_TITLE_1 = "Book title RestServiceIT 1";
	protected static final int TEST_BOOK_NBRPAGES_1 = 354;
	protected static final String TEST_BOOK_ISBN_1 = "1-84023-742-2";
	protected static final String TEST_BOOK_DESCRIPTION_1 = "Book description 1";
	protected static final String TEST_BOOK_PUBLICATION_DATE_1 = "1970-01-01";
	protected static final int TEST_BOOK_NBR_IN_INVENTORY_1 = 1;
	protected static final String TEST_BOOK_TITLE_2 = "Book title RestServiceIT 2";
	protected static final int TEST_BOOK_NBRPAGES_2 = 357;
	protected static final String TEST_BOOK_ISBN_2 = "1-84023-742-3";
	protected static final String TEST_BOOK_DESCRIPTION_2 = "Book description 2";
	protected static final String TEST_BOOK_PUBLICATION_DATE_2 = "1940-01-01";
	protected static final int TEST_BOOK_NBR_IN_INVENTORY_2 = 2;
	
	protected static final String TEST_LOAN_DATE_BORROWED_1 = "2088-05-04";
	protected static final String TEST_LOAN_DATE_DUE_1 = "2088-05-05";
	protected static final String TEST_LOAN_DATE_BORROWED_2 = "2088-05-06";
	protected static final String TEST_LOAN_DATE_DUE_2 = "2088-05-07";

	protected static User createTestUser1() {
		return new User(
				TEST_USER_DISPLAY_NAME_1, 
				TEST_USER_PASSWORD_1, // <- Since the hashed password has randomness,
				TEST_USER_ROLE_1,     //    we skip hashing and use a dummy String here.
				TEST_USER_FIRST_NAME_1, 
				TEST_USER_LAST_NAME_1, 
				TEST_USER_PHONE_1, 
				TEST_USER_EMAIL_1
		);
	}
	
	protected static User createTestUser2() {
		return new User(
				TEST_USER_DISPLAY_NAME_2, 
				TEST_USER_PASSWORD_2, // <- Since the hashed password has randomness,
				TEST_USER_ROLE_2,     //    we skip hashing and use a dummy String here.
				TEST_USER_FIRST_NAME_2, 
				TEST_USER_LAST_NAME_2, 
				TEST_USER_PHONE_2, 
				TEST_USER_EMAIL_2
		);
	}
	
	protected static Book createTestBook1() {
		return new Book(
				TEST_BOOK_TITLE_1, TEST_BOOK_DESCRIPTION_1, 
				TEST_BOOK_ISBN_1, TEST_BOOK_NBRPAGES_1,
				TEST_BOOK_PUBLICATION_DATE_1, TEST_BOOK_NBR_IN_INVENTORY_1
		);
	}
	
	protected static Book createTestBook2() {
		return new Book(
				TEST_BOOK_TITLE_2, TEST_BOOK_DESCRIPTION_2, 
				TEST_BOOK_ISBN_2, TEST_BOOK_NBRPAGES_2,
				TEST_BOOK_PUBLICATION_DATE_2, TEST_BOOK_NBR_IN_INVENTORY_2
		);
	}
	
	protected static final String BOOKS_BASE_URI =
			"http://localhost:8080/librarytest-rest/books";
	protected static final String AUTHORS_BASE_URI =
			"http://localhost:8080/librarytest-rest/authors";
	protected static final String USERS_BASE_URI =
			"http://localhost:8080/librarytest-rest/users";
	protected static final String LOANS_BASE_URI =
			"http://localhost:8080/librarytest-rest/loans";
	
	protected static Client client = ClientBuilder.newClient();
	protected static Response response;
	
	/**
	 * Removes from the database all data generated by these tests.
	 */
	@After
	public void cleanUpAfter() {
		
		if (response != null) {
			response.close();
		}
		
		// Loans 
		response = client.target(LOANS_BASE_URI).request().get();
		if (response.hasEntity()) {
			Loans loans = response.readEntity(Loans.class);
			response.close();
			for (Loan l : loans) {
				if (l.getDateBorrowed().equals(TEST_LOAN_DATE_BORROWED_1) || 
						l.getDateBorrowed().equals(TEST_LOAN_DATE_BORROWED_2)) {
					response = 
							client.target(LOANS_BASE_URI)
							.path(String.valueOf(l.getId()))
							.request()
							.delete();
					response.close();
				}
			}
		}
		
		// Books
		response = client.target(BOOKS_BASE_URI).request().get();
		if (response.hasEntity()) {
			Books books = response.readEntity(Books.class);
			response.close();
			for (Book b : books) {
				if (b.getTitle().startsWith(TEST_BOOK_TITLE_1)) {
					response = 
							client.target(BOOKS_BASE_URI)
							.path(String.valueOf(b.getId()))
							.request()
							.delete();
					response.close();
				}
			}
		}
		
		// Authors
		response = client.target(AUTHORS_BASE_URI).request().get();
		if (response.hasEntity()) {
			Authors authors = response.readEntity(Authors.class);
			response.close();
			for (Author a : authors) {
				if (a.getFirstName().startsWith(TEST_AUTHOR_FIRST_NAME_1) || 
						a.getFirstName().startsWith(TEST_AUTHOR_FIRST_NAME_2)) {
					response = 
							client.target(AUTHORS_BASE_URI)
							.path(String.valueOf(a.getId()))
							.request()
							.delete();
					response.close();
				}
			}
		}
		
		// Users
		response = client.target(USERS_BASE_URI).request().get();
		if (response.hasEntity()) {
			Users users = response.readEntity(Users.class);
			response.close();
			for (User u : users) {
				if (u.getFirstName().startsWith(TEST_USER_FIRST_NAME_1) || 
						u.getFirstName().startsWith(TEST_USER_FIRST_NAME_2)) {
					response = 
							client.target(USERS_BASE_URI)
							.path(String.valueOf(u.getId()))
							.request()
							.delete();
					response.close();
				}
			}
		}
	}
}