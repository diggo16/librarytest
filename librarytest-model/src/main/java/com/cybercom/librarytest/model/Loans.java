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
@XmlSeeAlso(Loan.class)
public class Loans extends ArrayList<Loan> {

	private static final long serialVersionUID = -6552522298451729237L;

	public Loans() {
		super();
	}

	public Loans(Collection<? extends Loan> c) {
		super(c);
	}

	@XmlElement(name = "loan")
	public List<Loan> getLoans() {
		return this;
	}

	public void setLoans(List<Loan> loans) {
		this.addAll(loans);
	}
}