package com.cybercom.librarytest.model;

import java.sql.Date;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JPA bean for representing a book in the library.
 * 
 * @author Lennart Moraeus
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = Book.FIND_ALL, query = "SELECT b FROM Book b"),
		@NamedQuery(
				name = Book.FIND_ALL_BY_AUTHOR, 
				query = "SELECT DISTINCT b "
						+ "FROM Book b, IN (b.authors) AS a " 
						+ "WHERE a.id = :id"
		)
})
public class Book extends BaseEntity implements Comparable<Book> {

	public static final String FIND_ALL = "Book.findAll";
	public static final String FIND_ALL_BY_AUTHOR = "Book.findAllByAuthor";

	@Column(nullable = false)
	private String title;
	@Column(length = 2000)
	private String description;
	@Column(length = 20)
	private String isbn;
	private Integer nbrPages;
	private Date publicationDate;
	@ManyToMany
	@JoinTable(name = "book_has_author")
	private List<Author> authors;
	private Integer totalNbrCopies;

	public Book() {
		this(null, null, null, null, null, null, null);
	}

	public Book(String title, String description, String isbn,
			Integer nbrPages, String publicationDate) {
		this(null, title, description, isbn, nbrPages, publicationDate, 0);
	}
	
	public Book(String title, String description, String isbn,
			Integer nbrPages, String publicationDate, Integer nbrInInventory) {
		this(null, title, description, isbn, nbrPages, publicationDate, nbrInInventory);
	}

	public Book(String title, String description, String isbn, Integer nbrPages) {
		this(null, title, description, isbn, nbrPages, null, 0);
	}

	public Book(Long id, String title, String description, String isbn,
			Integer nbrPages, String publicationDate, Integer nbrInInventory) {
		super(id);
		this.title = title;
		this.description = description;
		this.isbn = isbn;
		this.nbrPages = nbrPages;
		this.setPublicationDate(publicationDate);
		this.authors = new ArrayList<>();
		this.totalNbrCopies = nbrInInventory;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getNbrPages() {
		return nbrPages;
	}

	public void setNbrPages(Integer nbrPages) {
		this.nbrPages = nbrPages;
	}

	public String getPublicationDate() {
		return publicationDate.toString();
	}

	public void setPublicationDate(String publicationDate) {
		if (publicationDate != null) {
			try {
				this.publicationDate = Date.valueOf(publicationDate);
				return;
			} catch (DateTimeParseException e) {
				e.printStackTrace();
			}
		}
		this.publicationDate = new Date(0);
	}

	@XmlElement(name = "author")
	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public Integer getTotalNbrCopies() {
		return totalNbrCopies;
	}
	
	public void setTotalNbrCopies(Integer totalNbrCopies) {
		this.totalNbrCopies = totalNbrCopies;
	}
	
	@Override
	public int compareTo(Book other) {
		if (this.authors.size() == 0 || other.authors.size() == 0) {
			return this.title.compareTo(other.title);
		}
		return this.authors.get(0).compareTo(other.authors.get(0));
	}
	
	@Override
	public String toString() {
		return getTitle();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Book)) {
			return super.equals(other);
		}
		Book otherBook = (Book)other;
		return id.equals(otherBook.id) &&
				description.equals(otherBook.description) &&
				isbn.equals(otherBook.isbn) &&
				nbrPages.equals(otherBook.nbrPages) &&
				publicationDate.equals(otherBook.publicationDate) &&
				authors.equals(otherBook.authors) &&
				totalNbrCopies.equals(otherBook.totalNbrCopies);
	}
}
