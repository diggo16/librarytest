package com.cybercom.librarytest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JPA bean for representing a user in the library.
 * 
 * @author Lennart Moraeus
 */
@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u"), 
	@NamedQuery(
			name = User.FIND_WITH_DISPLAY_NAME, 
			query = "SELECT u FROM User u "
				   + "WHERE u.displayName = :name"
	)
})
@Table(name = "LIBUSER")
public class User extends BaseEntity implements Comparable<User> {
	public static final String FIND_ALL = "User.findAll";
	public static final String FIND_WITH_DISPLAY_NAME = "User.findWithName";

	public enum Role {
		LIBRARIAN,
		LOANER;
	}
	
	@Column(nullable = false, unique = true)
	private String displayName;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	
	public User() {
		super();
	}

	public User(Long id, String displayName, String password, Role role, 
			String firstName, String lastName, String phone, String email) {
		super(id);
		this.displayName = displayName;
		this.password = password;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
	}
	
	public User(String displayName, String password, Role role, 
			String firstName, String lastName, String phone, String email) {
		this.displayName = displayName;
		this.password = password;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
	}

	public User(String displayName, String password, Role role) {
		this.displayName = displayName;
		this.password = password;
		this.role = role;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof User)) {
			return false;
		}
		User otherUser = (User) other;
		return this.id.equals(otherUser.id)
				&& this.firstName.equals(otherUser.firstName)
				&& this.lastName.equals(otherUser.lastName)
				&& this.email.equals(otherUser.email)
				&& this.phone.equals(otherUser.phone);
	}

	@Override
	public int compareTo(User other) {
		return this.displayName.compareTo(other.displayName);
	}
	
	@Override
	public String toString() {
		return displayName;
	}
}
