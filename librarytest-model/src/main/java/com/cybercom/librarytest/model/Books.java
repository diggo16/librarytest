package com.cybercom.librarytest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Container for a list of books, to facilitate XML serialization.
 * @author Lennart Moraeus
 */
@XmlRootElement
@XmlSeeAlso({Book.class, Author.class})
public class Books extends ArrayList<Book> {

	private static final long serialVersionUID = -6552522298451729237L;

	public Books() {
		super();
	}

	public Books(Collection<? extends Book> c) {
		super(c);
	}

	@XmlElement(name = "book")
	public List<Book> getBooks() {
		return this;
	}

	public void setBooks(List<Book> books) {
		this.addAll(books);
	}
}