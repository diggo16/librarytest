package com.cybercom.librarytest.rest;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.jboss.resteasy.spi.BadRequestException;

import com.cybercom.librarytest.LocalEntityManagerFactory;
import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Authors;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Books;
import com.cybercom.librarytest.model.Loan;

/**
 * EJB implementing the REST interface for books in the library.
 * @author Lennart Moraeus
 */
@Path("/books")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BookRestService extends LibraryRestService {

	@Context
	private UriInfo uriInfo;

	/**
	 * Adds a new book. 
	 * @throws BadRequestException - if the book is null or if the book contains authors 
	 * with no id field set.
	 */
	@POST
	public Response createBook(Book book) {
		if (book == null) {
			throw new BadRequestException("Book was null.");
		} else if (doesBookExistInDatabase(book.getId())) {
			throw new BadRequestException("A book with this ID already exists.");
		} else if (book.getTitle() == null) {
			throw new BadRequestException("Book had no title.");
		}
		List<Author> authorsOfBook = book.getAuthors();
		for (Author authorOfBook : authorsOfBook) {
			if (authorOfBook.getId() == null) {
				throw new BadRequestException("Book contained an author with no id field set.");
			}
			confirmAuthorExistsInDatabaseWeak(authorOfBook);
		}
		if (book.getTotalNbrCopies() == null) {
			book.setTotalNbrCopies(0);
		}
		try {
			EntityManager em = LocalEntityManagerFactory.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.persist(book);
			tx.commit();
			em.close();
		} catch (DatabaseException e) {
			throw new BadRequestException(e.getMessage());
		}
		URI bookUri = uriInfo.getAbsolutePathBuilder()
				.path(String.valueOf(book.getId())).build();
		return Response.created(bookUri).build();
	}

	/**
	 * Updates the info about a book. 
	 * @throws BadRequestException - if the book is null or if the book contains authors 
	 * with no id field set.
	 */  
	@PUT
	public Response updateBook(Book book) {
		if (book == null) {
			throw new BadRequestException("Book was null.");
		} else if (book.getTitle() == null) {
			throw new BadRequestException("Book had no title.");
		}
		List<Author> authorsOfBook = book.getAuthors();
		for (Author authorOfBook : authorsOfBook) {
			if (authorOfBook.getId() == null) {
				throw new BadRequestException("Book contained an author with no id field set.");
			}
		}
		if (!doesBookExistInDatabase(book.getId())) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.merge(book);
		tx.commit();
		em.close();
		return Response.ok().build();
	}
	
	/**
	 * Returns all books in the database.
	 */
	@GET
	public Response getAllBooks() {
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL, Book.class);
		Books books = new Books(query.getResultList());
		return Response.ok(books).build();
	}
	
	/**
	 * Gets the book with the specified id.
	 * @throws NotFoundException - if no book exists with the given id.
	 */
	@GET
	@Path("{id}")
	public Response getBook(@PathParam("id") Long id) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Book book = em.find(Book.class, id);
		em.close();
		if (book == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(book).build();
	}

	/**
	 * Deletes the book with the specified id.
	 * NOTE: This also deletes all loans of this book.
	 * @throws NotFoundException - if no book exists with the given id. 
	 */
	@DELETE
	@Path("{id}")
	public Response deleteBook(@PathParam("id") Long id) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Book book = em.find(Book.class, id);
		if (book == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for (Loan loan : em.createNamedQuery(Loan.FIND_ALL_WITH_BOOK, Loan.class)
				.setParameter("id", id)
				.getResultList()) {
			em.remove(loan);
		}
		em.remove(book);
		tx.commit();
		return Response.noContent().build();
	}
	
	/**
	 * Returns all books by the specified author.
	 * @param authorId The id of the author.
	 * @return
	 */
	@GET
	@Path("/byauthor/{author_id}")
	public Response getBooksForAuthor(@PathParam("author_id")Long authorId) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL_BY_AUTHOR, Book.class);
		Books books = new Books(query.setParameter("id", authorId).getResultList());
		return Response.ok(books).build();
	}
	
	/**
	 * Returns the authors of a book.
	 * @param bookId The book id.
	 * @return
	 */
	@GET
	@Path("/{book_id}/authors")
	public Response getAuthorsOfBook(@PathParam("book_id")Long bookId) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Book book = em.find(Book.class, bookId);
		em.close();
		if (book == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(new Authors(book.getAuthors())).build();
	}
	
	/**
	 * Adds an author to a book.
	 * @param bookId The book id.
	 * @param author The author.
	 * @return
	 */
	@POST
	@Path("/{book_id}/authors")
	public Response addAuthorToBook(@PathParam("book_id")Long bookId, Author author) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Book book = em.find(Book.class, bookId);
		if (book == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Response response = addAuthorToBook(book, author, em);
		tx.commit();
		return response;
	}
	
	/**
	 * Modifies a book's author list.
	 * @param bookId The book id.
	 * @param author The author.
	 * @return
	 */
	@PUT
	@Path("/{book_id}/authors")
	public Response updateAuthorsOfBook(@PathParam("book_id")Long bookId, Authors authors) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Book book = em.find(Book.class, bookId);
		if (book == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Response response = null;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		book.getAuthors().clear();
		for (Author author : authors) {
			response = addAuthorToBook(book, author, em);
			if (!response.getStatusInfo().toString().equals("OK")) {
				em.close(); // <- If anything goes wrong, we abort without committing.
				return response; 
			}
		}
		tx.commit();
		em.close();
		return response;
	}
	
	/**
	 * Helper method for adding an author to a book. The caller is responsible for 
	 * managing the EntityManager (em should not be closed, and a transaction 
	 * must be ongoing).
	 * @throws BadRequestException If the action is not allowed.
	 */
	private Response addAuthorToBook(Book book, Author author, EntityManager em) {
		if (author.getId() == null) {
			throw new BadRequestException("Author must have id field set.");
		}
		if (book.getAuthors().contains(author)) {
			throw new BadRequestException("Author is already author of this book.");
		}
		confirmAuthorExistsInDatabaseWeak(author, em);
		book.getAuthors().add(author);
		return Response.ok().build();
	}
	
	/**
	 * Throws BadRequestException if the author does not exist in the database.
	 * Performs a "weak" check, meaning that only non-null fields in the author entity
	 * are checked against the authors in the database.
	 */
	private void confirmAuthorExistsInDatabaseWeak(Author author) {
		confirmAuthorExistsInDatabaseWeak(author, null);
	}
	
	/**
	 * Throws BadRequestException if the author does not exist in the database.
	 * Performs a "weak" check, meaning that only non-null fields in the author entity
	 * are checked against the authors in the database.
	 */
	private void confirmAuthorExistsInDatabaseWeak(Author author, EntityManager em) {
		boolean shouldClose = false;
		if (em == null) {
			em = LocalEntityManagerFactory.createEntityManager();
			shouldClose = true;
		}
		Author authorInDB = em.find(Author.class, author.getId());
		if (shouldClose) {
			em.close();
		}
		if (authorInDB == null || !authorInDB.equalsWeak(author)) {
			throw new BadRequestException("Author does not exist in database.");
		}
	}
}