package com.cybercom.librarytest.ui.layouts;

import java.util.ArrayList;
import java.util.List;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class LeftMenuLayout extends VerticalLayout {
	private static final long serialVersionUID = -8019180230951386957L;
	
	private List<Link> links;
	
	public LeftMenuLayout() {
		
		Panel panel = new Panel();
		
		links = new ArrayList<>();
		if (SecUtils.currentUserCan("view book")) {
			Link browseBooksLink = new Link(
					"Browse books", 
					new ExternalResource(HOME_URI + BrowseBooksLayout.BASE_URI)
			);
			browseBooksLink.setId("side-menu-link-browse-books");
			links.add(browseBooksLink);
		}
		if (SecUtils.currentUserCan("view author")) {
			Link browseAuthorsLink = new Link(
					"Browse authors", 
					new ExternalResource(HOME_URI + BrowseAuthorsLayout.BASE_URI)
			);
			browseAuthorsLink.setId("side-menu-link-browse-authors");
			links.add(browseAuthorsLink);
		}
		if (SecUtils.currentUserCan("add book")) {
			Link addBookLink = new Link(
					"Add book", 
					new ExternalResource(HOME_URI + CreateBookLayout.BASE_URI)
			);
			addBookLink.setId("side-menu-link-add-book");
			links.add(addBookLink);
		}
		if (SecUtils.currentUserCan("add author")) {
			Link addAuthorLink = new Link(
					"Add author", 
					new ExternalResource(HOME_URI + CreateAuthorLayout.BASE_URI)
			);
			addAuthorLink.setId("side-menu-link-add-author");
			links.add(addAuthorLink);
		}
		if (SecUtils.currentUserCan("add user")) {
			Link addUserLink = new Link(
					"New user", 
					new ExternalResource(HOME_URI + CreateUserLayout.BASE_URI)
			);
			addUserLink.setId("side-menu-link-add-user");
			links.add(addUserLink);
		}
		Link signInLink = new Link(
				"Sign in", 
				new ExternalResource(HOME_URI + LoginLayout.BASE_URI)
		);
		signInLink.setId("side-menu-link-sign-in");
		links.add(signInLink);
		Link signOutLink = new Link(
				"Sign out", 
				new ExternalResource(HOME_URI + LogoutLayout.BASE_URI)
		);
		signOutLink.setId("side-menu-link-sign-out");
		links.add(signOutLink);
		if (SecUtils.getCurrentUserName() != null) {
			Link myProfileLink = new Link(
					"My profile", 
					new ExternalResource(HOME_URI + UserProfileLayout.BASE_URI)
			);
			myProfileLink.setId("side-menu-link-my-profile");
			links.add(myProfileLink);
		}
		
		VerticalLayout linkLayout = new VerticalLayout();
		for (Link link : links) {
			linkLayout.addComponent(link);
		}
		panel.setContent(linkLayout);
		setSizeFull();
		panel.setSizeFull();
		addComponent(panel);
	    //setMargin(true);
	    setSpacing(true);
	}
}
