package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.server.Page;

public class CreateBookLayout extends PersistBookLayout {
	private static final long serialVersionUID = -9189102772819938221L;
	 
	public static final String BASE_URI = "/add/book";
	
	public CreateBookLayout() {
		super(null);
		if (!userCanAccess()) {
			return;
		}
		Page.getCurrent().setTitle("Add book");
		headerLabel.setValue("Add book");
	    persistBookButton.setDescription("Add book");
	    persistBookButton.setId("add-book-button");
	}
	
	@Override
	protected boolean userCanAccess() {
		return SecUtils.currentUserCan("add book");
	}
	
	@Override
	protected RestClient<Book>.Result persistBook(Book book) {
		return new RestClient<Book>().createEntity(book);
	}

	@Override
	protected String persistFailedMessage() {
		return "Unable to add book: ";
	}

	@Override
	protected String persistSucceededMessage() {
		return "Added book: ";
	}
}
