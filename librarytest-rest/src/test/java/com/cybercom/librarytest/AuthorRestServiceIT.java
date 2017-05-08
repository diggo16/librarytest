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
import com.cybercom.librarytest.model.Authors;

/**
 * Integration tests for the Author REST service.
 * @author Lennart Moraeus
 */
public class AuthorRestServiceIT extends RestServiceIntegrationTest {

	private static WebTarget target = client.target(AUTHORS_BASE_URI);

	private static final String XML_1_AUTHOR = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+   "<author>"
						+     "<bio>" + TEST_AUTHOR_BIO_1 + "</bio>"
						+     "<country>" + TEST_AUTHOR_COUNTRY_1 + "</country>"
						+     "<firstName>" + TEST_AUTHOR_FIRST_NAME_1 + "</firstName>"
						+     "<lastName>" + TEST_AUTHOR_LAST_NAME_1 + "</lastName>"
						+   "</author>";
	private static final String XML_2_AUTHORS = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<authors>"
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
			+ "</authors>";
	
	@Test
	public void shouldMarshallAnAuthor() throws JAXBException {
		// given
		Author author = new Author(TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1);
		StringWriter writer = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(Author.class);
		Marshaller m = context.createMarshaller();
		m.marshal(author, writer);

		// then
		assertEquals(XML_1_AUTHOR, writer.toString());
	}

	@Test
	public void shouldMarshallAListOfAuthors() throws JAXBException {
		Authors authors = new Authors();
		authors.add(new Author(TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1));
		authors.add(new Author(TEST_AUTHOR_FIRST_NAME_2, TEST_AUTHOR_LAST_NAME_2, 
				TEST_AUTHOR_COUNTRY_2, TEST_AUTHOR_BIO_2));
		StringWriter writer = new StringWriter();
		Class<?>[] classes = new Class<?>[2];
		classes[0] = Authors.class;
		classes[1] = Author.class;
		JAXBContext context = JAXBContext.newInstance(classes);
		Marshaller m = context.createMarshaller();
		m.marshal(authors, writer);
		assertEquals(XML_2_AUTHORS, writer.toString());
	}


	@Test
	public void shouldCreateUpdateAndDeleteAnAuthor() throws JAXBException {

		Author author = new Author(TEST_AUTHOR_FIRST_NAME_1, TEST_AUTHOR_LAST_NAME_1, 
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1);

		// POSTs (creates) an Author
		response = target.request()
				.post(Entity.entity(author, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		URI authorURI = response.getLocation();
		response.close();

		// PUTs (updates) the author
		String authorId = authorURI.toString().split("/")[5];
		Author updatedAuthor = new Author(
				Long.parseLong(authorId), // <- Same id, 
				"Updated", "author name", // <- updated name.
				TEST_AUTHOR_COUNTRY_1, TEST_AUTHOR_BIO_1
		);
		response = target.request()
				.put(Entity.entity(updatedAuthor, MediaType.APPLICATION_XML));
		assertEquals("OK", response.getStatusInfo().toString());
		response.close();
		
		// GETs the author by location, confirms the updated name
		response = client.target(authorURI).request().get();
		author = response.readEntity(Author.class);
		assertEquals("OK", response.getStatusInfo().toString());
		assertEquals("Updated", author.getFirstName());
		response.close();

		// GETs the author id and DELETEs it
		response = target.path(authorId).request().delete();
		assertEquals("No Content", response.getStatusInfo().toString());
		response.close();

		// GETs the author by location and confirms that it has been deleted
		response = client.target(authorURI).request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}

	@Test
	public void shouldNotFindTheAuthorID() throws JAXBException {

		// GETs an author with an unknown ID
		response = target.path("invalidID").request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}
}