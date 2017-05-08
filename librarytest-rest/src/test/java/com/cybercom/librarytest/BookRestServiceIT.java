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
import com.cybercom.librarytest.model.Books;

/**
 * Integration tests for the Book REST service.
 * @author Lennart Moraeus
 */
public class BookRestServiceIT extends RestServiceIntegrationTest {
	
	private static final WebTarget TARGET = client.target(BOOKS_BASE_URI);
	
	private static final String XML_1_BOOK = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
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
			+ "</book>";
	private static final String XML_2_BOOKS = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<books>"
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
			+ "</books>";

	@Test
	public void testshouldMarshallABook() throws JAXBException {
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
		StringWriter writer = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(Book.class);
		Marshaller m = context.createMarshaller();
		m.marshal(book, writer);
		
		// then
		assertEquals(XML_1_BOOK, writer.toString());
	}

	@Test
	public void shouldMarshallAListOfBooks() throws JAXBException {
		Books books = new Books();
		Book book = createTestBook1();
		book.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1
		));
		books.add(book);
		Book book2 = createTestBook2();
		book2.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1
		));
		book2.getAuthors().add(new Author(
				TEST_AUTHOR_FIRST_NAME_2, TEST_AUTHOR_LAST_NAME_2, 
				TEST_AUTHOR_COUNTRY_2, TEST_AUTHOR_BIO_2
		));
		books.add(book2);
		StringWriter writer = new StringWriter();
		Class<?>[] classes = new Class[2];
		classes[0] = Books.class;
		classes[1] = Book.class;
		JAXBContext context = JAXBContext.newInstance(classes);
		Marshaller m = context.createMarshaller();
		m.marshal(books, writer);
		
		assertEquals(XML_2_BOOKS, writer.toString());
	}
	
	@Test
	public void shouldCreateUpdateAndDeleteABook() throws JAXBException {
		
		Book book = new Book(TEST_BOOK_TITLE_1, TEST_BOOK_DESCRIPTION_1, 
				TEST_BOOK_ISBN_1, TEST_BOOK_NBRPAGES_1, TEST_BOOK_PUBLICATION_DATE_1);

		// POSTs (creates) a Book
		response = TARGET.request()
				.post(Entity.entity(book, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();
		URI bookURI = response.getLocation();

		// PUTs (updates) the book
		String bookId = bookURI.toString().split("/")[5];
		Book updatedBook = createTestBook1();           // <- Other info same,
		updatedBook.setId(Long.parseLong(bookId));      // <- same id,
		updatedBook.setTitle("The Hitchhiker's Guide"); // <- updated title.
		response = TARGET.request()
				.put(Entity.entity(updatedBook, MediaType.APPLICATION_XML));
		assertEquals("OK", response.getStatusInfo().toString());
		response.close();
		
		// GETs the book by location, confirms the updated title
		response = client.target(bookURI).request().get();
		book = response.readEntity(Book.class);
		assertEquals("OK", response.getStatusInfo().toString());
		assertEquals("The Hitchhiker's Guide", book.getTitle());
		response.close();

		// GETs the book id and DELETEs it
		response = TARGET.path(bookId).request().delete();
		assertEquals("No Content", response.getStatusInfo().toString());
		response.close();

		// GETs the Book by location and confirms it has been deleted
		response = client.target(bookURI).request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}

	@Test
	public void shouldNotFindInvalidBookID() throws JAXBException {

		// GETs a Book with an unknown ID
		response = TARGET.path("invalidID").request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}
}