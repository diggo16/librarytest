package com.cybercom.librarytest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Container for a list of users, to facilitate XML serialization.
 * @author Lennart Moraeus
 */
@XmlRootElement
@XmlSeeAlso(User.class)
public class Users extends ArrayList<User> {

	private static final long serialVersionUID = -6552522298451729237L;

	public Users() {
		super();
	}

	public Users(Collection<? extends User> c) {
		super(c);
	}

	@XmlElement(name = "user")
	public List<User> getUsers() {
		return this;
	}

	public void setAuthors(List<User> users) {
		this.addAll(users);
	}
}