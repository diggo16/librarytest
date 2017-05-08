package com.cybercom.librarytest.ui;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Authors;
import com.cybercom.librarytest.model.BaseEntity;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Books;
import com.cybercom.librarytest.model.Loan;
import com.cybercom.librarytest.model.Loans;
import com.cybercom.librarytest.model.User;

public class RestClient <EntityType extends BaseEntity> {
	public static final String REST_BASE_URI = 
			"http://localhost:8080/librarytest-rest";
	public static final String BOOKS_BASE_URI   = REST_BASE_URI + "/books";
	public static final String AUTHORS_BASE_URI = REST_BASE_URI + "/authors";
	public static final String USERS_BASE_URI   = REST_BASE_URI + "/users";
	public static final String LOANS_BASE_URI   = REST_BASE_URI + "/loans";
	private static final Client CLIENT = ClientBuilder.newClient();
	private static Response response;
	
	public RestClient() {}
	
	public static Authors getAllAuthors() {
		response = CLIENT.target(AUTHORS_BASE_URI)
				.request()
				.get();
		Authors authors = response.readEntity(Authors.class);
		response.close();
		return authors;
	}
	
	public static Books getAllBooks() {
		response = CLIENT.target(BOOKS_BASE_URI)
				.request()
				.get();
		Books books = response.readEntity(Books.class);
		response.close();
		return books;
	}
	
	public static Book getBook(Long id) {
		response = CLIENT.target(BOOKS_BASE_URI).path(id.toString())
				.request()
				.get();
		Book book;
		if (response.getStatusInfo().toString().equals("OK")) {
			book = response.readEntity(Book.class);
		} else {
			book = null;
		}
		response.close();
		return book;		
	}
	
	public static Author getAuthor(Long id) {
		response = CLIENT.target(AUTHORS_BASE_URI).path(id.toString())
				.request()
				.get();
		Author author;
		if (response.getStatusInfo().toString().equals("OK")) {
			author = response.readEntity(Author.class);
		} else {
			author = null;
		}
		response.close();
		return author;
	}
	
	public static Books getBooksByAuthor(Author author) {
		response = CLIENT.target(BOOKS_BASE_URI)
				.path("/byauthor/" + author.getId().toString())
				.request()
				.get();
		Books books;
		if (response.getStatusInfo().toString().equals("OK")) {
			books = response.readEntity(Books.class);
		} else {
			books = new Books();
		}
		response.close();
		return books;
	}
	
	/**
	 * Creates a new entity in the database.
	 * If successful, the returned Result contains the REST URI of the newly created Book.
	 * If unsuccessful, the returned Result contains the HTTP status text.
	 */
	public Result createEntity(EntityType newEntity) {
		response = CLIENT.target(getBaseUri(newEntity))
				.request()
				.post(Entity.entity(newEntity, MediaType.APPLICATION_XML));
		Result result;
		if (response.getStatusInfo().toString().equals("Created")) {
			result = new Result(true, response.getLocation().toString());
		} else {
			result = new Result("Unable to create entity: ", response);
		}
		response.close();
		return result;
	}
	
	/**
	 * Update an entity in the database.
	 */
	public Result updateEntity(EntityType entity) {
		String baseUri = getBaseUri(entity);
		if (baseUri == null) {
			return new Result("Unknown entity type.");
		}
		response = CLIENT.target(baseUri)
				.request()
				.put(Entity.entity(entity, MediaType.APPLICATION_XML));
		Result result;
		if (response.getStatusInfo().toString().equals("OK")) {
			result = new Result();
		} else {
			result = new Result("Unable to update entity: ", response);
		}
		response.close();
		return result;
	}
		
	/**
	 * Delete an entity in the database.
	 * Returns true if successful.
	 */	
	public Result deleteEntity(EntityType entity) {
		String baseUri = getBaseUri(entity);
		if (baseUri == null) {
			return new Result("Unable to delete: Unknown entity type.");
		}
		response = CLIENT.target(baseUri)
				.path(entity.getId().toString())
				.request()
				.delete();
		Result result = null;
		if (response.getStatusInfo().toString().equals("No Content")) {
			result = new Result(true);
		} else {
			result = new Result("Unable to delete entity: ", response);
		}
		response.close();
		return result;		
	}
	
	/**
	 * Returns the user with the specified ID, if any such user exists.
	 * If no user is found, returns null.
	 */
	public static User getUserWithId(Long id) {
		if (id == null) {
			return null;
		}
		response = CLIENT.target(USERS_BASE_URI)
				.path(id.toString())
				.request()
				.get();
		User user = null;
		if (response.getStatusInfo().toString().equals("OK")) {
			user = response.readEntity(User.class);
		}
		response.close();
		return user;
	}
	
	/**
	 * Returns the user with the specified displayName, if any such user exists.
	 * If no user is found, returns null.
	 */
	public static User getUserWithName(String displayName) {
		if (displayName == null) {
			return null;
		}
		response = CLIENT.target(USERS_BASE_URI)
				.path("withname/" + displayName)
				.request()
				.get();
		User user = null;
		if (response.getStatusInfo().toString().equals("OK")) {
			user = response.readEntity(User.class);
		}
		response.close();
		return user;
	}
	
	/**
	 * Returns the loans with the specified ID, if any such loan exists.
	 * If no loan is found, returns null.
	 */
	public static Loan getLoanWithId(Long id) {
		if (id == null) {
			return null;
		}
		response = CLIENT.target(LOANS_BASE_URI)
				.path(id.toString())
				.request()
				.get();
		Loan loan = null;
		if (response.getStatusInfo().toString().equals("OK")) {
			loan = response.readEntity(Loan.class);
		}
		response.close();
		return loan;
	}
	
	/**
	 * Returns all loans of the specified user, if any such loans exists.
	 * Otherwise, returns null.
	 */
	public static Loans getAllLoansOfUser(User user) {
		if (user == null || user.getId() == null) {
			return null;
		}
		response = CLIENT.target(LOANS_BASE_URI)
				.path("ofuser")
				.path(user.getId().toString())
				.request()
				.get();
		Loans loans;
		if (!response.getStatusInfo().toString().equals("OK")) {
			loans = new Loans();
		} else {
			loans = response.readEntity(Loans.class);
		}
		response.close();
		return loans.size() > 0 ? loans : null;
	}
	
	/**
	 * Returns all loans of the specified user, if any such loans exists.
	 * Otherwise, returns an empty Loans instance.
	 */
	public static Loans getAllLoansOfBook(Book book) {
		if (book == null || book.getId() == null) {
			return new Loans();
		}
		response = CLIENT.target(LOANS_BASE_URI)
				.path("ofbook")
				.path(book.getId().toString())
				.request()
				.get();
		Loans loans;
		if (!response.getStatusInfo().toString().equals("OK")) {
			loans = new Loans();
		} else {
			loans = response.readEntity(Loans.class);
		}
		response.close();
		return loans;
	}
	
	/**
	 * Returns the loan of the specified user and book, if any such loan exists.
	 * Otherwise, returns null.
	 */
	public static Loan getLoanOfUserAndBook(User user, Book book) {
		if (user == null || user.getId() == null) {
			return null;
		}
		if (book == null || book.getId() == null) {
			return null;
		}
		response = CLIENT.target(LOANS_BASE_URI)
				.path("ofuser")
				.path(user.getId().toString())
				.path("ofbook")
				.path(book.getId().toString())
				.request()
				.get();
		Loan loan;
		if (response.getStatusInfo().toString().equals("OK")) {
			loan = response.readEntity(Loan.class);
		} else {
			loan = null;
		}
		response.close();
		return loan;
	}
	
	private String getBaseUri(EntityType entity) {
		if (entity instanceof Author) {
			return AUTHORS_BASE_URI;
		} else if (entity instanceof Book) {
			return BOOKS_BASE_URI;
		} else if (entity instanceof User) {
			return USERS_BASE_URI;
		} else if (entity instanceof Loan) {
			return LOANS_BASE_URI;
		} else {
			return null;
		}
	}
	
	/**
	 * Describes the result of a REST API call.
	 * @author Lennart Moraeus 
	 *
	 */
	public class Result {
		public boolean success;
		public String msg;
		
		public Result() {
			this(true);
		}
		
		public Result(boolean success) {
			this.success = success;
		}
		
		public Result(String msg) {
			this.success = false;
			this.msg = msg;
		}
		
		public Result(String error, Response response) {
			this(false);
			this.msg = error + response.getStatusInfo().toString();
			String responseBody = response.readEntity(String.class); 
			if (responseBody != null && responseBody.length() > 0) {
				this.msg += ", " + responseBody;
			}
		}
		
		public Result(boolean success, String msg) {
			this.success = success;
			this.msg = msg;
		}
		
		public String toString() {
			return msg;
		}
	}
}
