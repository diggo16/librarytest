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

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.model.Users;

/**
 * Integration tests for the User REST service.
 * @author Lennart Moraeus
 */
public class UserRestServiceIT extends RestServiceIntegrationTest {

	private static final WebTarget TARGET = client.target(USERS_BASE_URI);

	private static final String XML_1_USER = 
		  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+   "<user>"
			+     "<displayName>" + TEST_USER_DISPLAY_NAME_1 + "</displayName>"
			+     "<email>" + TEST_USER_EMAIL_1 + "</email>"
			+     "<firstName>" + TEST_USER_FIRST_NAME_1 + "</firstName>"
			+     "<lastName>" + TEST_USER_LAST_NAME_1 + "</lastName>"
			+     "<password>" + TEST_USER_PASSWORD_1 + "</password>"
			+     "<phone>" + TEST_USER_PHONE_1 + "</phone>"
			+     "<role>" + TEST_USER_ROLE_1 + "</role>"
			+   "</user>";
	private static final String XML_2_USERS = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+ "<users>"
			+   "<user>"
			+     "<displayName>" + TEST_USER_DISPLAY_NAME_1 + "</displayName>"
			+     "<email>" + TEST_USER_EMAIL_1 + "</email>"
			+     "<firstName>" + TEST_USER_FIRST_NAME_1 + "</firstName>"
			+     "<lastName>" + TEST_USER_LAST_NAME_1 + "</lastName>"
			+     "<password>" + TEST_USER_PASSWORD_1 + "</password>"
			+     "<phone>" + TEST_USER_PHONE_1 + "</phone>"
			+     "<role>" + TEST_USER_ROLE_1 + "</role>"
			+   "</user>"			
			+   "<user>"
			+     "<displayName>" + TEST_USER_DISPLAY_NAME_2 + "</displayName>"
			+     "<email>" + TEST_USER_EMAIL_2 + "</email>"
			+     "<firstName>" + TEST_USER_FIRST_NAME_2 + "</firstName>"
			+     "<lastName>" + TEST_USER_LAST_NAME_2 + "</lastName>"
			+     "<password>" + TEST_USER_PASSWORD_2 + "</password>"
			+     "<phone>" + TEST_USER_PHONE_2 + "</phone>"
			+     "<role>" + TEST_USER_ROLE_2 + "</role>"
			+   "</user>"
			+ "</users>";
	
	@Test
	public void shouldMarshallAUser() throws JAXBException {
		// given
		User user = createTestUser1();
		StringWriter writer = new StringWriter();  
		JAXBContext context = JAXBContext.newInstance(User.class);
		Marshaller m = context.createMarshaller();
		m.marshal(user, writer);

		// then
		assertEquals(XML_1_USER, writer.toString());
	}

	@Test
	public void shouldMarshallAListOfUsers() throws JAXBException {
		Users users = new Users();
		User user1 = createTestUser1();
		users.add(user1);
		User user2 = createTestUser2();
		users.add(user2);
		StringWriter writer = new StringWriter();
		Class<?>[] classes = new Class<?>[2];
		classes[0] = Users.class;
		classes[1] = User.class;
		JAXBContext context = JAXBContext.newInstance(classes);
		Marshaller m = context.createMarshaller();
		m.marshal(users, writer);
		assertEquals(XML_2_USERS, writer.toString());
	}


	@Test
	public void shouldCreateUpdateAndDeleteAUser() throws JAXBException {

		User user = createTestUser1();

		// POSTs (creates) a user
		response = TARGET.request()
				.post(Entity.entity(user, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		URI userURI = response.getLocation();
		response.close();

		// PUTs (updates) the user
		String userId = userURI.toString().split("/")[5];
		User updatedUser = createTestUser1();
		updatedUser.setId(Long.parseLong(userId));// <-
		updatedUser.setFirstName("Updated");      // <- same info 
		updatedUser.setLastName("User name");     // <- except name
		response = TARGET.request()
				.put(Entity.entity(updatedUser, MediaType.APPLICATION_XML));
		assertEquals("OK", response.getStatusInfo().toString());
		response.close();
		
		// GETs the user by location, confirms the updated name
		response = client.target(userURI).request().get();
		user = response.readEntity(User.class);
		assertEquals("OK", response.getStatusInfo().toString());
		assertEquals("Updated", user.getFirstName());
		response.close();

		// DELETEs the user
		response = TARGET.path(userId).request().delete();
		assertEquals("No Content", response.getStatusInfo().toString());
		response.close();

		// GETs the user by location and confirms that it has been deleted
		response = client.target(userURI).request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}

	@Test
	public void shouldNotFindUnknownUserID() throws JAXBException {

		// GETs an user with an unknown ID
		response = TARGET.path("invalidID").request().get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}
	
	@Test
	public void shouldFindUserByName() throws JAXBException {

		User user = createTestUser1();

		// POSTs (creates) a user
		response = TARGET.request()
				.post(Entity.entity(user, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		URI userURI = response.getLocation();
		Long userId = Long.parseLong(userURI.toString().split("/")[5]);
		response.close();
		
		// GETs the user by name, confirm correct user ID
		response = TARGET.path("withname/" + TEST_USER_DISPLAY_NAME_1)
				.request()
				.get();
		user = response.readEntity(User.class);
		assertEquals(userId, user.getId());
		response.close();
	}
	
	@Test
	public void shouldNotFindUserByUnknownName() throws JAXBException {

		User user = createTestUser1();

		// POSTs (creates) a user
		response = TARGET.request()
				.post(Entity.entity(user, MediaType.APPLICATION_XML));
		assertEquals("Created", response.getStatusInfo().toString());
		response.close();
		
		// GETs the user by unknown name, confirm that user is not found
		response = TARGET.path("withname/" + TEST_USER_DISPLAY_NAME_1 + "unknown")
				.request()
				.get();
		assertEquals("Not Found", response.getStatusInfo().toString());
		response.close();
	}
	
//	/**
//	 * Removes from the database all data generated by these tests.
//	 */
//	@After
//	public void cleanUpAfter() {
//		response = client.target(USERS_BASE_URI).request().get();
//		if (response.hasEntity()) {
//			Users users = response.readEntity(Users.class);
//			response.close();
//			for (User u : users) {
//				if (u.getDisplayName().equals(TEST_USER_DISPLAY_NAME_1) || 
//						u.getDisplayName().equals(TEST_USER_DISPLAY_NAME_2)) {
//					response = 
//							client.target(USERS_BASE_URI)
//							.path(String.valueOf(u.getId()))
//							.request()
//							.delete();
//					response.close();
//				}
//			}
//		}
//	}
}