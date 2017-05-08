package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Loan;
import static com.cybercom.librarytest.ui.MyUI.HOME_URI;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.cybercom.librarytest.ui.ViewUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class SingleBookLayout extends VerticalLayout {
	private static final long serialVersionUID = -9004839388733872159L;

	public static final String BASE_URI = "/book/";
	
	public static String uri(Book book) {
		return HOME_URI + BASE_URI + book.getId();
	}
	
	public SingleBookLayout(Long bookId) {
		Book book = RestClient.getBook(bookId);
		if (book == null) {
			addComponent(new Label("Invalid book."));
			return;
		}
		Page.getCurrent().setTitle("View book: " + book);
		Image image = ViewUtils.createImageBook();
		Label headerLabel = new Label("View book");
		headerLabel.setId("main-content-header");
		Label titleLabel = new Label(book.getTitle());
		titleLabel.setCaption("Title: ");
		HorizontalLayout authorsLayout = 
				ViewUtils.createAuthorsLinkLayout(book.getAuthors());
		Label descriptionLabel = new Label(book.getDescription());
		descriptionLabel.setCaption("Description: ");
		Label nbrPagesLabel = new Label(book.getNbrPages().toString());
		nbrPagesLabel.setCaption("Number of pages: ");
		Label isbnLabel = new Label(book.getIsbn());
		isbnLabel.setCaption("ISBN: ");
		Label datePublishedLabel = new Label(book.getPublicationDate());
		datePublishedLabel.setCaption("Date published: ");
		int nbrCopiesAvailable = getNbrCopiesAvailable(book);
		Label nbrAvailableLabel = new Label(""+nbrCopiesAvailable);
		nbrAvailableLabel.setCaption("Number of copies available: ");
		addComponents(image, headerLabel, titleLabel, authorsLayout, 
				descriptionLabel, isbnLabel, datePublishedLabel, nbrAvailableLabel);
		if (SecUtils.currentUserCan("edit book")) {
			Label totalNbrCopiesLabel = new Label(book.getTotalNbrCopies().toString());
			totalNbrCopiesLabel.setCaption("Total number of copies: ");
			addComponent(totalNbrCopiesLabel);
		}
		HorizontalLayout buttonLayout = new HorizontalLayout(); 
		if (SecUtils.currentUserCan("borrow book")) {
			Loan loan = RestClient.getLoanOfUserAndBook(SecUtils.getCurrentUser(), book);
			if (loan != null) {
				buttonLayout.addComponent(ViewUtils.createReturnBookButton(book));
			} else if (nbrCopiesAvailable > 0){
				buttonLayout.addComponent(ViewUtils.createBorrowBookButton(book));
			}
		}
		if (SecUtils.currentUserCan("edit book")) {
			buttonLayout.addComponent(ViewUtils.createEditBookButton(book));
		}
		if (SecUtils.currentUserCan("delete book")) {
			buttonLayout.addComponent(ViewUtils.createDeleteEntityButton(book));
		}
		if (buttonLayout.getComponentCount() > 0) {
			buttonLayout.setSpacing(true);
			addComponent(buttonLayout);
		}
	    setMargin(true);
	    setSpacing(true);
	}
	
	private static int getNbrCopiesAvailable(Book book) {
		return book.getTotalNbrCopies() - RestClient.getAllLoansOfBook(book).size();
	}
}
