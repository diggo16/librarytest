package com.cybercom.librarytest.rest;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
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
import com.cybercom.librarytest.model.Loan;
import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.model.Users;

/**
 * EJB implementing the REST interface for users in the library.
 * @author Lennart Moraeus
 */
@Path("/users")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class UserRestService extends LibraryRestService {

	@Context
	private UriInfo uriInfo;

	/**
	 * Adds a new user.
	 * @throws BadRequestException - if the specified user is null or 
	 * if any required field is null or if the displayName already exists.
	 */
	@POST
	public Response createUser(User user) {
		if (user == null) {
			throw new BadRequestException("User was null.");
		} else if (doesUserExistInDatabase(user.getId())) {
			throw new BadRequestException("User already exists.");
		} else if (user.getPassword() == null) {
			throw new BadRequestException("Password was not set.");
		} else if (user.getRole() == null) {
			throw new BadRequestException("Role was not set.");
		} else if (user.getDisplayName() == null) {
			throw new BadRequestException("Display name was not set.");
		} else if (doGetUserWithName(user.getDisplayName()) != null) {
			throw new BadRequestException("User with same display name already exists.");
		} 
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(user);
		tx.commit();
		em.close();
		URI userUri = uriInfo.getAbsolutePathBuilder()
				.path(String.valueOf(user.getId())).build();
		return Response.created(userUri).build();
	}

	/**
	 * Updates the info about a user.
	 * @throws BadRequestException - if the specified user is null.
	 */  
	@PUT
	public Response updateUser(User user) {
		if (user == null) {
			throw new BadRequestException("User was null.");
		} else if (user.getId() == null) {
			throw new BadRequestException("Id was not set.");
		} else if (user.getPassword() == null) {
			throw new BadRequestException("Password was not set.");
		} else if (user.getRole() == null) {
			throw new BadRequestException("Role was not set.");
		} else if (user.getDisplayName() == null) {
			throw new BadRequestException("Display name was not set.");
		} else if (!doesUserExistInDatabase(user.getId())) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		User userWithSameDisplayName = 
				doGetUserWithName(user.getDisplayName());
		if (userWithSameDisplayName != null && 
				!userWithSameDisplayName.getId().equals(user.getId())) {
			throw new BadRequestException("Another user with same display name already exists.");
		}
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.merge(user);
		tx.commit();
		em.close();
		return Response.ok().build();
	}

	/**
	 * Returns all users in the database.
	 */
	@GET
	public Response getAllUsers() {
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL, User.class);
		Users users = new Users(query.getResultList());
		Response r = Response.ok(users).build();
		em.close();
		return r;
	}
	
	/**
	 * Gets the user with the specified id.
	 * @throws NotFoundException - if no user exists with the given id.
	 */
	@GET
	@Path("{id}")
	public Response getUser(@PathParam("id") Long id) {
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		User user = em.find(User.class, id);
		em.close();
		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(user).build();
	}

	/**
	 * Deletes the user with the specified id.
	 * NOTE: This also deletes all loans of this user.
	 * @throws NotFoundException - if no user exists with the given id. 
	 */
	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") Long id) {
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		User user = em.find(User.class, id);
		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for (Loan loan : em.createNamedQuery(Loan.FIND_ALL_WITH_USER, Loan.class)
				.setParameter("id", id)
				.getResultList()) {
			em.remove(loan);
		}
		em.remove(user);
		tx.commit();
		em.close();
		return Response.noContent().build();
	}
	
	/**
	 * Gets the user with the specified display name.
	 * @throws NotFoundException - if no user exists with the given display name.
	 */
	@GET
	@Path("withname/{name}")
	public Response getUserWithName(@PathParam("name") String displayName) {
		User user = doGetUserWithName(displayName);
		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(user).build();
	}
	
	private User doGetUserWithName(String displayName) {
		EntityManager em = 
				LocalEntityManagerFactory.createEntityManager();
		TypedQuery<User> query = 
				em.createNamedQuery(User.FIND_WITH_DISPLAY_NAME, User.class)
				.setParameter("name", displayName); 
		User user = null;		
		try {
			user = query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		em.close();
		return user;
	}
}