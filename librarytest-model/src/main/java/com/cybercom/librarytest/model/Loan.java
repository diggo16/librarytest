package com.cybercom.librarytest.model;

import java.sql.Date;
import java.time.format.DateTimeParseException;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JPA bean for representing a loan in the library.
 * 
 * @author Lennart Moraeus
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = Loan.FIND_ALL, query = "SELECT l FROM Loan l"),
		@NamedQuery(
				name = Loan.FIND_ALL_WITH_USER, 
				query = "SELECT DISTINCT l "
						+ "FROM Loan l " 
					   + "WHERE l.user.id = :id"
		), 
		@NamedQuery(
				name = Loan.FIND_ALL_WITH_BOOK, 
				query = "SELECT DISTINCT l "
						+ "FROM Loan l " 
					   + "WHERE l.book.id = :id"
		), 
		@NamedQuery(
				name = Loan.FIND_ALL_WITH_BOOK_AND_USER, 
				query = "SELECT DISTINCT l "
						+ "FROM Loan l " 
					   + "WHERE l.book.id = :book_id "
					     + "AND l.user.id = :user_id"
		)
})
public class Loan extends BaseEntity {
	public static final String FIND_ALL = "Loan.findAll";
	public static final String FIND_ALL_WITH_USER = "Loan.findAllWithUser";
	public static final String FIND_ALL_WITH_BOOK = "Loan.findAllWithBook";
	public static final String FIND_ALL_WITH_BOOK_AND_USER = "Loan.findAllWithBookAndUser";
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BOOK_ID")
	private Book book;
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	private Date dateBorrowed;
	private Date dateDue;
	
	public Loan() {
		this(null, null, null, null, null);
	}
	
	public Loan(Book book, User user, String dateBorrowedStr, String dateDueStr) {
		this(null, book, user, dateBorrowedStr, dateDueStr);
	}
	
	public Loan(Long id, Book book, User user, String dateBorrowedStr, String dateDueStr) {
		super(id);
		this.book = book;
		this.user = user;
		setDateBorrowed(dateBorrowedStr);
		setDateDue(dateDueStr);
	}
	
	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getDateBorrowed() {
		return String.valueOf(dateBorrowed);
	}
	
	public Date getDateBorrowedAsDate() {
		return dateBorrowed;
	}
	
	public void setDateBorrowed(String dateBorrowedStr) {
		if (dateBorrowedStr != null) {
			try {
				this.dateBorrowed = Date.valueOf(dateBorrowedStr);
				return;
			} catch (DateTimeParseException e) {
				e.printStackTrace();
			}
		}
		this.dateBorrowed = new Date(0);
	}
	
	public void setDateBorrowed(Date dateBorrowed) {
		this.dateBorrowed = dateBorrowed;
	}
	
	public String getDateDue() {
		return String.valueOf(dateDue);
	}
	
	public Date getDateDueAsDate() {
		return dateDue;
	}
	
	public void setDateDue(String dateDueStr) {
		if (dateDueStr != null) {
			try {
				this.dateDue = Date.valueOf(dateDueStr);
				return;
			} catch (DateTimeParseException e) {
				e.printStackTrace();
			}
		}
		this.dateDue = new Date(0);
	}
	
	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Loan)) {
			return super.equals(other);
		}
		Loan otherLoan = (Loan)other;
		return id.equals(otherLoan.id) &&
				book.equals(otherLoan.book) &&
				user.equals(otherLoan.user) &&
				dateBorrowed.equals(otherLoan.dateBorrowed) &&
				dateDue.equals(otherLoan.dateDue);
	}
}
