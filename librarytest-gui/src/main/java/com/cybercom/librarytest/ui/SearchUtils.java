package com.cybercom.librarytest.ui;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Authors;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Books;

public abstract class SearchUtils {
	
	/**
	 * Returns all authors whose names match the specified String.
	 * A match happens when:
	 * 1. The String contains the first and last name
	 * 2. The first or last name contains the String
	 */
	public static Authors filterAuthorsForName(String name, Authors authors) {
		Authors matchingAuthors = new Authors();
		for (Author author : authors) {
			boolean isMatch = false;
			if (name.contains(author.getFirstName()) && 
					name.contains(author.getLastName())) {
				isMatch = true;
			} else if (author.getFirstName().contains(name) ||
					author.getLastName().contains(name)) {
				isMatch = true;
			}
			if (isMatch) {
				matchingAuthors.add(author);
			}
		}
		return matchingAuthors;
	}
	
	/**
	 * Returns all authors whose names match the specified String.
	 * A match happens when:
	 * 1. The country contains the String
	 */
	public static Authors filterAuthorsForCountry(String country, Authors authors) {
		Authors matchingAuthors = new Authors();
		for (Author author : authors) {
			if (author.getCountry().contains(country)) {
				matchingAuthors.add(author);
			}
		}
		return matchingAuthors;
	}
	
	/**
	 * Returns all books where the titles match the specified String.
	 * A match happens when:
	 * 1. The String contains the title
	 * 2. The title contains the String
	 */
	public static Books filterBooksForTitle(String title, Books books) {
		Books matchingBooks = new Books();
		for (Book book : books) {
			if (title.contains(book.getTitle()) || 
					book.getTitle().contains(title)) {
				matchingBooks.add(book);
			}
		}
		return matchingBooks;
	}
	
	/**
	 * Returns all books where the isbn match the specified String.
	 * A match happens when:
	 * 1. The String contains the isbn
	 * 2. The isbn contains the String
	 */
	public static Books filterBooksForIsbn(String isbn, Books books) {
		Books matchingBooks = new Books();
		for (Book book : books) {
			if (isbn.contains(book.getIsbn()) || 
					book.getIsbn().contains(isbn)) {
				matchingBooks.add(book);
			}
		}
		return matchingBooks;
	}
	
	/**
	 * Returns all books where the date published matches the specified String.
	 * A match happens when:
	 * 1. The String representation of the date published contains the String
	 */
	public static Books filterBooksForDatePublished(String datePublished, Books books) {
		Books matchingBooks = new Books();
		for (Book book : books) {
			if (book.getPublicationDate().toString().contains(datePublished)) {
				matchingBooks.add(book);
			}
		}
		return matchingBooks;
	}
	
	/**
	 * Returns all books where an author matches the specified String.
	 * A match happens when:
	 * 1. The String contains the title
	 * 2. The title contains the String
	 */
	public static Books filterBooksForAuthorName(String authorName, Books books) {
		Authors matchingAuthors = 
				filterAuthorsForName(authorName, RestClient.getAllAuthors());
		return filterBooksForAuthors(matchingAuthors, books);
	}
	
	/**
	 * Returns all books where an author is among the specified list of authors.
	 * A match happens when:
	 * 1. The String contains the title
	 * 2. The title contains the String
	 */
	public static Books filterBooksForAuthors(Authors authors, Books books) {
		Books matchingBooks = new Books();
		for (Book book : books) {
			boolean isMatch = false;
			for (Author authorOfCurrentBook : book.getAuthors()) {
				for (Author author : authors) {
					if (authorOfCurrentBook.equals(author)) {
						isMatch = true;
					}
				}
			}
			if (isMatch) {
				matchingBooks.add(book);
			}
		}
		return matchingBooks;
	}
}
