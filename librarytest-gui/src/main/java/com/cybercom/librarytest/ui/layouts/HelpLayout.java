package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.ui.ViewUtils;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

public class HelpLayout extends VerticalLayout {
	private static final long serialVersionUID = 6386140785158507379L;
	public static final String BASE_URI = "/help";
	
	public HelpLayout() {
		Page.getCurrent().setTitle("Library Test Application - Help");
		addComponent(ViewUtils.createImageBookshelf());
		addComponent(new Label("Welcome to the Library Test Application!"));
		addComponent(new Label(
				"To borrow books, first select 'New user' to create a new account. "
			  + "Then, browse books and authors and select 'Borrow book' to borrow "
			  + "a book."));
		addComponent(new Label(
				"There are two types of users in the Library Test Application: "
			  + "Loaners and Librarians. Loaners can browse books authors and "
			  + "borrow books, as long as there are copies left in the inventory. "
			  + "Librarians are system administrators and have more privileges."));	
		addComponent(new Label(
				"To add new books and authors you need to log in as a Librarian. "
			  + "Use credentials: Username: 'admin', Password: '1234567890'. "
			  + "Librarians are able to edit most information, including "
			  + "changing Loaner accounts into Librarians. "));
		addComponent(new Label(
				"There are some restrictions when editing information. "
			  + "For example, it is not possible to delete an author until "
			  + "all the author's books have been deleted. Also, all fields "
			  + "marked with a red star are mandatory when creating books, authors "
			  + "and users. "));
		addComponent(new Label(
				"It is also possible to access the library system using a REST "
			  + "service. To see the REST service documentation, follow the link "
			  + "below. The documentation also contains a full description of the "
			  + "restrictions when dealing with books, authors, users and loans. "));
		addComponent(new Link("REST resource documentation", 
				new ThemeResource("readme.html")));
	    setMargin(true);
	    setSpacing(true);
	}
}
