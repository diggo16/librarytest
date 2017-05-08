package com.cybercom.librarytest;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Loan;
import com.cybercom.librarytest.model.Loans;
import com.cybercom.librarytest.model.User;

/**
 * Integration tests for the Loan REST service.
 * @author Lennart Moraeus
 */
public class LoanRestServiceIT extends RestServiceIntegrationTest {
	
	private static final WebTarget TARGET = client.target(LOANS_BASE_URI);
	
	private static final String XML_1_LOAN = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<loan>"
			+ "<book>"
			+   "<author>"
			+     "<bio>" + TEST_AUTHOR_BIO_1 + "</bio>"
			+     "<country>" + TEST_AUTHOR_COUNTRY_1 + "</country>"
			+     "<firstName>" + TEST_AUTHOR_FIRST_NAME_1 + "</firstName>"
			+     "<lastName>" + TEST_AUTHOR_LAST_NAME_1 + "</lastName>"
			+   "</author>"
			+   "<author>"
			+     "<bio>" + TEST_AUTHOR_BIO_2 + "</bio>"
			+     "<country>" + TEST_AUTHOR_COUNTRY_2 + "</country>"
			+     "<firstName>" + TEST_AUTHOR_FIRST_NAME_2 + "</firstName>"
			+     "<lastName>" + TEST_AUTHOR_LAST_NAME_2 + "</lastName>"
			+   "</author>"
			+   "<description>" + TEST_BOOK_DESCRIPTION_1 + "</description>"
			+   "<isbn>" + TEST_BOOK_ISBN_1 + "</isbn>"
			+   "<nbrPages>" + TEST_BOOK_NBRPAGES_1 + "</nbrPages>"
			+   "<publicationDate>" + TEST_BOOK_PUBLICATION_DATE_1 + "</publicationDate>"
			+   "<title>" + TEST_BOOK_TITLE_1 + "</title>"
			+   "<totalNbrCopies>" + TEST_BOOK_NBR_IN_INVENTORY_1 + "</totalNbrCopies>"
			+ "</book>"
			+ "<dateBorrowed>" + TEST_LOAN_DATE_BORROWED_1 + "</dateBorrowed>"
			+ "<dateDue>" + TEST_LOAN_DATE_DUE_1 + "</dateDue>"
			+ "<user>"
			+   "<displayName>" + TEST_USER_DISPLAY_NAME_1 + "</displayName>"
			+   "<email>" + TEST_USER_EMAIL_1 + "</email>"
			+   "<firstName>" + TEST_USER_FIRST_NAME_1 + "</firstName>"
			+   "<lastName>" + TEST_USER_LAST_NAME_1 + "</lastName>"
			+   "<password>" + TEST_USER_PASSWORD_1 + "</password>"
			+   "<phone>" + TEST_USER_PHONE_1 + "</phone>"
			+   "<role>" + TEST_USER_ROLE_1 + "</role>"
			+ "</user>"
			+ "</loan>";
	private static final String XML_2_LOANS = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<loans>"
			+ "<loan>"	  
			+ "<book>"
			+   "<author>"
			+     "<bio>" + TEST_AUTHOR_BIO_1 + "</bio>"
			+     "<country>" + TEST_AUTHOR_COUNTRY_1 + "</country>"
			+     "<firstName>" + TEST_AUTHOR_FIRST_NAME_1 + "</firstName>"
			+     "<lastName>" + TEST_AUTHOR_LAST_NAME_1 + "</lastName>"
			+   "</author>"
			+   "<description>" + TEST_BOOK_DESCRIPTION_1 + "</description>"
			+   "<isbn>" + TEST_BOOK_ISBN_1 + "</isbn>"
			+   "<nbrPages>" + TEST_BOOK_NBRPAGES_1 + "</nbrPages>"
			+   "<publicationDate>" + TEST_BOOK_PUBLICATION_DATE_1 + "</publicationDate>"
			+   "<title>" + TEST_BOOK_TITLE_1 + "</title>"
			+   "<totalNbrCopies>" + TEST_BOOK_NBR_IN_INVENTORY_1 + "</totalNbrCopies>"
			+ "</book>"
			+ "<dateBorrowed>" + TEST_LOAN_DATE_BORROWED_1 + "</dateBorrowed>"
			+ "<dateDue>" + TEST_LOAN_DATE_DUE_1 + "</dateDue>"
			+ "<user>"
			+   "<displayName>" + TEST_USER_DISPLAY_NAME_1 + "</displayName>"
			+   "<email>" + TEST_USER_EMAIL_1 + "</email>"
			+   "<firstName>" + TEST_USER_FIRST_NAME_1 + "</firstName>"
			+   "<lastName>" + TEST_USER_LAST_NAME_1 + "</lastName>"
			+   "<password>" + TEST_USER_PASSWORD_1 + "</password>"
			+   "<phone>" + TEST_USER_PHONE_1 + "</phone>"
			+   "<role>" + TEST_USER_ROLE_1 + "</role>"
			+ "</user>"
			+ "</loan>"
			+ "<loan>"
			+ "<book>"
			+   "<author>"
			+     "<bio>" + TEST_AUTHOR_BIO_1 + "</bio>"
			+     "<country>" + TEST_AUTHOR_COUNTRY_1 + "</country>"
			+     "<firstName>" + TEST_AUTHOR_FIRST_NAME_1 + "</firstName>"
			+     "<lastName>" + TEST_AUTHOR_LAST_NAME_1 + "</lastName>"
			+   "</author>"
			+   "<author>"
			+     "<bio>" + TEST_AUTHOR_BIO_2 + "</bio>"
			+     "<country>" + TEST_AUTHOR_COUNTRY_2 + "</country>"
			+     "<firstName>" + TEST_AUTHOR_FIRST_NAME_2 + "</firstName>"
			+     "<lastName>" + TEST_AUTHOR_LAST_NAME_2 + "</lastName>"
			+   "</author>"
			+   "<description>" + TEST_BOOK_DESCRIPTION_2 + "</description>"
			+   "<isbn>" + TEST_BOOK_ISBN_2 + "</isbn>"
			+   "<nbrPages>" + TEST_BOOK_NBRPAGES_2 + "</nbrPages>"
			+   "<publicationDate>" + TEST_BOOK_PUBLICATION_DATE_2 + "</publicationDate>"
			+   "<title>" + TEST_BOOK_TITLE_2 + "</title>"
			+   "<totalNbrCopies>" + TEST_BOOK_NBR_IN_INVENTORY_2 + "</totalNbrCopies>"
			+ "</book>"
			+ "<dateBorrowed>" + TEST_LOAN_DATE_BORROWED_2 + "</dateBorrowed>"
			+ "<dateDue>" + TEST_LOAN_DATE_DUE_2 + "</dateDue>"
			+ "<user>"
			+   "<displayName>" + TEST_USER_DISPLAY_NAME_2 + "</displayName>"
			+   "<email>" + TEST_USER_EMAIL_2 + "</email>"
			+   "<firstName>" + TEST_USER_FIRST_NAME_2 + "</firstName>"
			+  "<lastName>" + TEST_USER_LAST_NAME_2 + "</lastName>"
			+   "<password>" + TEST_USER_PASSWORD_2 + "</password>"
			+   "<phone>" + TEST_USER_PHONE_2 + "</phone>"
			+   "<role>" + TEST_USER_ROLE_2 + "</role>"
			+ "</user>"
			+ "</loan>"
			+ "</loans>";

	@Test
	public void shouldMarshallALoan() throws JAXBException {
		// given
		Book book = createTestBook1();
		book.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1
		));
		book.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_2, TEST_AUTHOR_LAST_NAME_2, 
				TEST_AUTHOR_COUNTRY_2, TEST_AUTHOR_BIO_2
		));
		User user = createTestUser1();
		Loan loan = new Loan(book, user, TEST_LOAN_DATE_BORROWED_1, 
				TEST_LOAN_DATE_DUE_1);
		StringWriter writer = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(Loan.class);
		Marshaller m = context.createMarshaller();
		m.marshal(loan, writer);
		
		// then
		assertEquals(XML_1_LOAN, writer.toString());
	}

	@Test
	public void shouldMarshallAListOfLoans() throws JAXBException {
		Loans loans = new Loans();
		Book book = createTestBook1();
		book.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1
		));
		User user = createTestUser1();
		Loan loan = new Loan(book, user, TEST_LOAN_DATE_BORROWED_1, 
				TEST_LOAN_DATE_DUE_1);
		loans.add(loan);
		Book book2 = createTestBook2();
		book2.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1
		));
		book2.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_2, TEST_AUTHOR_LAST_NAME_2, 
				TEST_AUTHOR_COUNTRY_2, TEST_AUTHOR_BIO_2
		));
		User user2 = createTestUser2();
		Loan loan2 = new Loan(book2, user2, TEST_LOAN_DATE_BORROWED_2, 
				TEST_LOAN_DATE_DUE_2);
		loans.add(loan2);
		StringWriter writer = new StringWriter();
		Class<?>[] classes = new Class[2];
		classes[0] = Loans.class;
		classes[1] = Loan.class;
		JAXBContext context = JAXBContext.newInstance(classes);
		Marshaller m = context.createMarshaller();
		m.marshal(loans, writer);
		
		assertEquals(XML_2_LOANS, writer.toString());
	}
	
	@Test
	public void shouldCreateUpdateAndDeleteALoan() throws JAXBException {
		
		// POSTs a User and a Book so that we can use them for the Loan test.
		User user = createTestUser1();
		Book book = createTestBook1();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		
		// POSTs (creates) a Loan
		Loan loan = createTestLoan(user, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();
		URI loanURI = response.getLocation();

		// PUTs (updates) the loan
		String loanId = loanURI.toString().split("/")[5];
		Loan updatedLoan = createTestLoan(user, book); // <- Other info same,
		updatedLoan.setId(Long.parseLong(loanId));     // <- same id,
		updatedLoan.setDateDue(TEST_LOAN_DATE_DUE_2);  // <- updated due date.
		response = TARGET.request()
				.put(Entity.entity(updatedLoan, MediaType.APPLICATION_XML));
		assertEquals("OK", response.getStatusInfo().toString());
		response.close();
		
		// GETs the loan by location, confirms the updated due date
		response = client.target(loanURI).request().get();
		loan = response.readEntity(Loan.class);
		assertEquals("OK", response.getStatusInfo().toString());
		assertEquals(TEST_LOAN_DATE_DUE_2, loan.getDateDue());
		response.close();

		// DELETEs the loan by id 
		response = TARGET.path(loanId).request().delete();
		assertEquals("No Content", response.getStatusInfo().toString());
		response.close();

		// GETs the Book by location and confirms it has been deleted
		response = client.target(loanURI).request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}

	@Test
	public void shouldNotUpdateLoanWithNewBook() throws JAXBException {
		
		// POSTs a User and a Book so that we can use them for the Loan test.
		User user = createTestUser1();
		Book book1 = createTestBook1();
		Book book2 = createTestBook2();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		
		// POSTs (creates) a Loan
		Loan loan = createTestLoan(user, book1);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();
		URI loanURI = response.getLocation();

		// PUTs (updates) the loan with new book
		String loanId = loanURI.toString().split("/")[5];
		loan.setId(Long.parseLong(loanId)); // <- same id,
		loan.setBook(book2);         // <- updated book.
		response = TARGET.request()
				.put(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Bad Request", response.getStatusInfo().toString());
		response.close();
	}
	
	@Test
	public void shouldNotUpdateLoanWithNewUser() throws JAXBException {
		
		// POSTs a User and a Book so that we can use them for the Loan test.
		User user1 = createTestUser1();
		User user2 = createTestUser2();
		Book book = createTestBook1();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();

		// POSTs (creates) a Loan
		Loan loan = createTestLoan(user1, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();
		URI loanURI = response.getLocation();

		// PUTs (updates) the loan with new user
		String loanId = loanURI.toString().split("/")[5];
		loan.setId(Long.parseLong(loanId)); // <- same id,
		loan.setUser(user2);                // <- updated book.
		response = TARGET.request()
				.put(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Bad Request", response.getStatusInfo().toString());
		response.close();
	}
	
	@Test
	public void shouldGetLoansOfUser() throws JAXBException {
		
		// POST a User and two Books so that we can use them for the Loan test.
		User user = createTestUser1();
		Book book1 = createTestBook1();
		Book book2 = createTestBook2();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();

		// POST two Loans between the user and the two books
		Loan loan1 = createTestLoan(user, book1);
		response = TARGET.request()
				.post(Entity.entity(loan1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		Loan loan2 = createTestLoan(user, book2);
		response = TARGET.request()
				.post(Entity.entity(loan2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		
		// GET the list of the user's loans, confirm that both loans are there
		response = TARGET
				.path("ofuser")
				.path(user.getId().toString())
				.request()
				.get();
		assertEquals("OK", response.getStatusInfo().toString());
		Loans loans = response.readEntity(Loans.class);
		response.close();
		assertEquals(2, loans.size());
		assertEquals(
				loan1.getId().toString() + loan1.getDateBorrowed() + loan1.getBook(), 
				loans.get(0).getId().toString() + loans.get(0).getDateBorrowed()
						+ loans.get(0).getBook()
		);
		assertEquals(
				loan2.getId().toString() + loan2.getDateBorrowed() + loan2.getBook(), 
				loans.get(1).getId().toString() + loans.get(1).getDateBorrowed()
						+ loans.get(1).getBook()
		);
	}
	
	@Test
	public void shouldGetLoansOfBook() throws JAXBException {
		
		// POST a Book and two Users so that we can use them for the Loan test.
		User user1 = createTestUser1();
		User user2 = createTestUser2();
		Book book = createTestBook1();
		book.setTotalNbrCopies(2);
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();

		// POST two Loans between the two users and the book
		Loan loan1 = createTestLoan(user1, book);
		response = TARGET.request()
				.post(Entity.entity(loan1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		Loan loan2 = createTestLoan(user2, book);
		response = TARGET.request()
				.post(Entity.entity(loan2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		
		// GET the list of the book's loans, confirm that both loans are there
		response = TARGET
				.path("ofbook")
				.path(book.getId().toString())
				.request()
				.get();
		assertEquals("OK", response.getStatusInfo().toString());
		Loans loans = response.readEntity(Loans.class);
		response.close();
		assertEquals(2, loans.size());
		assertEquals(
				loan1.getId().toString() + loan1.getDateBorrowed() + loan1.getUser(), 
				loans.get(0).getId().toString() + loans.get(0).getDateBorrowed()
						+ loans.get(0).getUser()
		);
		assertEquals(
				loan2.getId().toString() + loan2.getDateBorrowed() + loan1.getUser(), 
				loans.get(1).getId().toString() + loans.get(1).getDateBorrowed()
						+ loans.get(0).getUser()
		);
	}
	
	@Test
	public void shouldGetLoanOfUserAndBook() throws JAXBException {
		
		// POST two Books and two Users so that we can use them for the Loan test.
		User user1 = createTestUser1();
		User user2 = createTestUser2();
		Book book1 = createTestBook1();
		Book book2 = createTestBook2();
		book1.setTotalNbrCopies(2);
		book2.setTotalNbrCopies(2);
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();

		// POST four Loans: each user borrows both books
		Loan loan1 = createTestLoan(user1, book1);
		response = TARGET.request()
				.post(Entity.entity(loan1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		Loan loan2 = createTestLoan(user1, book2);
		response = TARGET.request()
				.post(Entity.entity(loan2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		Loan loan3 = createTestLoan(user2, book1);
		response = TARGET.request()
				.post(Entity.entity(loan3, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan3.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		Loan loan4 = createTestLoan(user2, book2);
		response = TARGET.request()
				.post(Entity.entity(loan4, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		loan4.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		
		// GET User1's loan of Book1, confirm that the Loan, User and Book entities are correct
		// Use resource /ofuser/{u_id}/ofbook/{b_id}
		response = TARGET
				.path("ofuser")
				.path(user1.getId().toString())
				.path("ofbook")
				.path(book1.getId().toString())
				.request()
				.get();
		assertEquals("OK", response.getStatusInfo().toString());
		Loan loanFromDb = response.readEntity(Loan.class);
		response.close();
		assertEquals(
				loan1.getId().toString() + loan1.getDateBorrowed() + 
						loan1.getUser() + loan1.getBook(), 
				loanFromDb.getId().toString() + loanFromDb.getDateBorrowed() + 
						loanFromDb.getUser() + loanFromDb.getBook()
		);
		
		// GET User2's loan of Book2, confirm that the Loan, User and Book entities are correct
		// Use resource /ofbook/{b_id}/ofuser/{u_id}
		response = TARGET
				.path("ofbook")
				.path(book2.getId().toString())
				.path("ofuser")
				.path(user2.getId().toString())
				.request()
				.get();
		assertEquals("OK", response.getStatusInfo().toString());
		loanFromDb = response.readEntity(Loan.class);
		response.close();
		assertEquals(
				loan4.getId().toString() + loan4.getDateBorrowed() + 
						loan4.getUser() + loan4.getBook(), 
				loanFromDb.getId().toString() + loanFromDb.getDateBorrowed() + 
						loanFromDb.getUser() + loanFromDb.getBook()
		);
	}
	
	@Test
	public void shouldNotCreateLoanIfBookNotInStock() throws JAXBException {
		
		// POST one Book and two Users so that we can use them for the Loan test.
		// The book has 1 sample in inventory.
		User user1 = createTestUser1();
		User user2 = createTestUser2();
		Book book = createTestBook1();
		book.setTotalNbrCopies(1);
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();

		// POST a Loan - user 1 borrows the book
		// this brings the number of available samples of this book from 1 to 0 
		Loan loan = createTestLoan(user1, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();

		// POST another Loan - user 2 tries to borrow the book
		// confirm that it is not possible
		loan = createTestLoan(user2, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Conflict", response.getStatusInfo().toString());
		response.close();
	}
	
	@Test
	public void shouldCreateLoanAfterIncreasingNbrInStock() throws JAXBException {
		
		// POST one Book and two Users so that we can use them for the Loan test.
		// The book has 1 sample in inventory.
		User user1 = createTestUser1();
		User user2 = createTestUser2();
		Book book = createTestBook1();
		book.setTotalNbrCopies(1);
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();

		// POST a Loan - user 1 borrows the book
		// this brings the number of available samples of this book from 1 to 0 
		Loan loan = createTestLoan(user1, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();

		// POST another Loan - user 2 tries to borrow the book
		// confirm that it is not possible
		loan = createTestLoan(user2, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Conflict", response.getStatusInfo().toString());
		response.close();
		
		// Update the book using PUT 
		book.setTotalNbrCopies(2); // <- increase nbr from 1 to 2.
		response = client.target(BOOKS_BASE_URI)
				.request()
				.put(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("OK", response.getStatusInfo().toString());
		response.close();
		
		// POST another Loan - user 2 tries to borrow the book again
		// confirm that the loan is created this time
		loan = createTestLoan(user2, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();
	}
	
	@Test
	public void shouldCreateLoanAfterReturningOtherLoan() throws JAXBException {
		
		// POST one Book and two Users so that we can use them for the Loan test.
		// The book has 1 sample in inventory.
		User user1 = createTestUser1();
		User user2 = createTestUser2();
		Book book = createTestBook1();
		book.setTotalNbrCopies(1);
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user1, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user1.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(USERS_BASE_URI).request()
				.post(Entity.entity(user2, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		user2.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();
		response = client.target(BOOKS_BASE_URI).request()
				.post(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		book.setId(Long.parseLong(response.getLocation().toString().split("/")[5]));
		response.close();

		// POST a Loan - user 1 borrows the book
		// this brings the number of available samples of this book from 1 to 0 
		Loan loan = createTestLoan(user1, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		String loanId = response.getLocation().toString().split("/")[5];
		response.close();

		// POST another Loan - user 2 tries to borrow the book
		// confirm that it is not possible
		loan = createTestLoan(user2, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Conflict", response.getStatusInfo().toString());
		response.close();
		
		// DELETE the first loan
		// this brings the number of available samples of this book back to 1
		response = TARGET
				.path(loanId)
				.request()
				.delete();
		assertEquals("No Content", response.getStatusInfo().toString());
		response.close();
		
		// POST another Loan - user 2 tries to borrow the book again
		// confirm that the loan is created this time
		loan = createTestLoan(user2, book);
		response = TARGET.request()
				.post(Entity.entity(loan, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();
	}
	
	@Test
	public void shouldNotFindInvalidLoanID() throws JAXBException {

		// GETs a Book with an unknown ID
		response = TARGET.path("invalidID").request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}
	
	private Loan createTestLoan(User user, Book book) {
		Loan loan = new Loan(book, user, TEST_LOAN_DATE_BORROWED_1, 
				TEST_LOAN_DATE_DUE_1);
		return loan;
	}
}