package com.cybercom.librarytest.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JPA bean for representing an author in the library.
 * 
 * @author Lennart Moraeus
 */
@Entity
@XmlRootElement
@NamedQuery(name = Author.FIND_ALL, query = "SELECT a FROM Author a")
public class Author extends BaseEntity implements Comparable<Author> {

	public static final String FIND_ALL = "Author.findAll";

	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false)
	private String country;
	@Column(nullable = false, length = 2000)
	private String bio;

	public Author() {
		this(null, null, null, null, null);
	}

	public Author(String firstName, String lastName,  
			String country, String bio) {
		this(null, firstName, lastName, country, bio);
	}
	
	public Author(Long id, String firstName, String lastName, 
			String country, String bio) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.bio = bio;
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
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * Returns true if all fields in this entity match the
	 * corresponding fields in the other entity.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Author)) {
			return false;
		}
		Author otherAuthor = (Author) other;
		return this.id.equals(otherAuthor.id)
				&& this.firstName.equals(otherAuthor.firstName)
				&& this.lastName.equals(otherAuthor.lastName)
				&& this.country.equals(otherAuthor.country)
				&& this.bio.equals(otherAuthor.bio);
	}
	
	/**
	 * Returns true if all non-null fields in this entity match the
	 * corresponding non-null fields in the other entity.
	 */
	public boolean equalsWeak(Author other) {
		if (this.id != null && other.id != null &&
				!this.id.equals(other.id)) {
			return false;
		}
		if (this.firstName != null && other.firstName != null &&
				!this.firstName.equals(other.firstName)) {
			return false;
		}
		if (this.lastName != null && other.lastName != null &&
				!this.lastName.equals(other.lastName)) {
			return false;
		}
		if (this.country != null && other.country != null &&
				!this.country.equals(other.country)) {
			return false;
		}
		if (this.bio != null && other.bio != null &&
				!this.bio.equals(other.bio)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Author other) {
		return this.lastName.compareTo(other.lastName);
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
