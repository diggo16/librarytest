package com.cybercom.librarytest.rest;

import java.net.URI;
import java.sql.Date;

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

import org.jboss.resteasy.spi.BadRequestException;

import com.cybercom.librarytest.LocalEntityManagerFactory;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Loan;
import com.cybercom.librarytest.model.Loans;

/**
 * EJB implementing the REST interface for loans in the library.
 * @author Lennart Moraeus
 */
@Path("/loans")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class LoanRestService extends LibraryRestService {
	@Context
	private UriInfo uriInfo;
	
	private static final long LOAN_DURATION_MS = 3 * 24 * 60 * 60 * 1000; // 3 days
	
	/**
	 * Adds a new loan. 
	 * Date due and date borrowed are auto-generated if they are not set.
	 * If both are missing, the current date is used for date borrowed and date due is 
	 * set to LOAN_DURATION_MS milliseconds after that.
	 * If only one is missing, the other is set to LOAN_DURATION_MS milliseconds before 
	 * or after the one that is present.
	 * @throws BadRequestException - if the loan is null, if the there is already a 
	 * loan with the same user and book, if the specified book is not in inventory or 
	 * if the book or the user does not exist.
	 */
	@POST
	public Response createLoan(Loan loan) {
		
		// First enforce all restrictions, throw exception if necessary. 
		if (loan == null) {
			throw new BadRequestException("Loan was null.");
		} else if (loan.getUser() == null || 
				!doesUserExistInDatabase(loan.getUser().getId())) {
			throw new BadRequestException("User does not exist.");
		} else if (loan.getBook() == null || 
				!doesBookExistInDatabase(loan.getBook().getId())) {
			throw new BadRequestException("Book does not exist.");
		} else if (doesLoanExistInDatabase(loan.getId())) {
			return Response.status(Response.Status.CONFLICT)
					.entity("Same loan already exists.")
					.build();
		}
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Book book = em.find(Book.class, loan.getBook().getId());
		int nbrLoansOfBook = 0;
		try {
			nbrLoansOfBook = getLoansOfBookList(book.getId()).size();
		} catch(NotFoundException e) { /* No action */ }
		if (nbrLoansOfBook >= book.getTotalNbrCopies()) {
			return Response.status(Response.Status.CONFLICT)
					.entity("No copies of the book left in inventory.")
					.build();
		}
		
		// If we get this far, OK to create the loan.
		if (loan.getDateBorrowed() == null && loan.getDateBorrowed() == null ) {
			long currentTimeMs = new java.util.Date().getTime();
			loan.setDateBorrowed(new Date(currentTimeMs));
			loan.setDateDue(new Date(currentTimeMs + LOAN_DURATION_MS));
		} else if (loan.getDateBorrowed() == null) {
			loan.setDateBorrowed(new Date(loan.getDateDueAsDate().getTime() 
					- LOAN_DURATION_MS));
		} else if (loan.getDateDue() == null) {
			loan.setDateBorrowed(new Date(loan.getDateBorrowedAsDate().getTime()
					+ LOAN_DURATION_MS));
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(loan);
		tx.commit();
		em.close();
		URI loanUri = uriInfo.getAbsolutePathBuilder()
				.path(String.valueOf(loan.getId())).build();
		return Response.created(loanUri).build();
	}
	
	/**
	 * Updates the info about a loan. 
	 * @throws BadRequestException - if the loan is null, if the loan does not have 
	 * an id set, if the loan does not have date borrowed or date due set, or if 
	 * the user or book of the loan has changed.
	 */
	@PUT
	public Response updateLoan(Loan loan) {
		
		// First enforce all conditions, throw exception if necessary. 
		if (loan == null) {
			throw new BadRequestException("Loan was null.");
		} else if (loan.getId() == null) {
			throw new BadRequestException("No id set in loan.");
		} else if (loan.getDateBorrowed() == null) {
			throw new BadRequestException("Date borrowed was not set.");
		} else if (loan.getDateDue() == null) {
			throw new BadRequestException("Date due was not set.");
		}
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Loan oldLoan = em.find(Loan.class, loan.getId());
		if (oldLoan == null) {
			throw new NotFoundException("Loan not found.");
		} else if (!loan.getBook().equals(oldLoan.getBook())) {
			throw new BadRequestException("Can't update loan with new book.");
		} else if (!loan.getUser().equals(oldLoan.getUser())) {
			throw new BadRequestException("Can't update loan with new user.");
		}
		
		// If we get this far, OK to update the loan.
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.merge(loan);
		tx.commit();
		em.close();
		return Response.ok().build();
	}
	
	/**
	 * Returns all loans in the database.
	 */
	@GET
	public Response getAllLoans() {
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		TypedQuery<Loan> query = em.createNamedQuery(Loan.FIND_ALL, Loan.class);
		Loans loans = new Loans(query.getResultList());
		return Response.ok(loans).build();
	}
	
	/**
	 * Gets the loan with the specified id.
	 * @throws NotFoundException - if no loan exists with the given id.
	 */
	@GET
	@Path("{id}")
	public Response getLoan(@PathParam("id") Long id) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Loan loan = em.find(Loan.class, id);
		em.close();
		if (loan == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(loan).build();
	}
	
	/**
	 * Deletes the laon with the specified id.
	 * @throws NotFoundException - if no loan exists with the given id. 
	 */
	@DELETE
	@Path("{id}")
	public Response deleteLoan(@PathParam("id") Long id) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		Loan loan = em.find(Loan.class, id);
		if (loan == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.remove(loan);
		tx.commit();
		return Response.noContent().build();
	}
	
	/**
	 * Returns all loans of the specified user.
	 */
	@GET
	@Path("/ofuser/{user_id}")
	public Response getLoansOfUser(@PathParam("user_id")Long userId) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		TypedQuery<Loan> query = em.createNamedQuery(Loan.FIND_ALL_WITH_USER, Loan.class);
		Loans loans = new Loans(query.setParameter("id", userId).getResultList());
		if (loans == null || loans.size() == 0) {
			throw new NotFoundException();
		}
		return Response.ok(loans).build();
	}
	
	/**
	 * Returns all loans of the specified book.
	 */
	@GET
	@Path("/ofbook/{book_id}")
	public Response getLoansOfBook(@PathParam("book_id")Long bookId) {
		return Response.ok(getLoansOfBookList(bookId)).build();
	}
	
	private Loans getLoansOfBookList(Long bookId) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		TypedQuery<Loan> query = em.createNamedQuery(Loan.FIND_ALL_WITH_BOOK, Loan.class);
		Loans loans = new Loans(query.setParameter("id", bookId).getResultList());
		if (loans == null || loans.size() == 0) {
			throw new NotFoundException();
		}
		return loans;
	}
	
	/**
	 * Returns the loan of the specified user and the specified book, if it exists.
	 * @throws NotFoundException if the loan does not exist
	 */
	@GET
	@Path("/ofuser/{user_id}/ofbook/{book_id}")
	public Response getLoansOfUserAndBook(@PathParam("user_id")Long userId, 
			@PathParam("book_id")Long bookId) {
		return doGetLoansOfUserAndBook(userId, bookId);
	}
	
	/**
	 * Returns the loan of the specified book and the specified user, if it exists.
	 * @throws NotFoundException if the loan does not exist
	 */
	@GET
	@Path("/ofbook/{book_id}/ofuser/{user_id}")
	public Response getLoansOfBookAndUser(@PathParam("book_id")Long bookId, 
			@PathParam("user_id")Long userId) {
		return doGetLoansOfUserAndBook(userId, bookId);
	}
	
	private Response doGetLoansOfUserAndBook(Long userId, Long bookId) {
		EntityManager em = LocalEntityManagerFactory.createEntityManager();
		TypedQuery<Loan> query = em.createNamedQuery(Loan.FIND_ALL_WITH_BOOK_AND_USER, Loan.class);
		Loans loans = new Loans(
				query.setParameter("book_id", bookId)
				.setParameter("user_id", userId)
				.getResultList()
		);
		if (loans == null || loans.size() == 0) {
			throw new NotFoundException();
		}
		return Response.ok(loans.get(0)).build();
	}
}
