package com.cybercom.librarytest.ui.layouts;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.Label;

public class EditBookLayout extends PersistBookLayout {
	private static final long serialVersionUID = -9189102772819938221L;
	
	public static String uri(Book book) {
		return HOME_URI + BASE_URI + book.getId();
	}
	
	public static final String BASE_URI = "/edit/book/";
	
	private static final RestClient<Book> REST_CLIENT = new RestClient<>();  
	 
	public EditBookLayout(Long bookId) {
		super(RestClient.getBook(bookId));
		if (!userCanAccess() || book == null || book.getId() == null) {
			removeAllComponents();
			addComponent(new Label("Invalid book."));
			return;
		}
	    if (persistBookButton != null) {
	    	persistBookButton.setCaption("Save changes");
	    	persistBookButton.setId("save-book-button");
	    }
	    headerLabel.setValue("Edit book");
	    Page.getCurrent().setTitle("Edit book");
	}
	
	@Override
	protected boolean userCanAccess() {
		return SecUtils.currentUserCan("edit book");
	}
	
	@Override
	protected RestClient<Book>.Result persistBook(Book book) {
		return REST_CLIENT.updateEntity(book);
	}

	@Override
	protected String persistFailedMessage() {
		return "Unable to save changes to book: ";
	}

	@Override
	protected String persistSucceededMessage() {
		return "Saved changes to book: ";
	}
}
