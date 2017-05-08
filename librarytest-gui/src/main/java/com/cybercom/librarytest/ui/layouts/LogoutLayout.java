package com.cybercom.librarytest.ui.layouts;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.ui.RestClient;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class LogoutLayout extends VerticalLayout {
	private static final long serialVersionUID = -1965841374346400996L;
	
	public static final String BASE_URI = "/user/logout";
	
	public LogoutLayout() {
		// TODO: Should redirect to front page after signing out?
		Page.getCurrent().setTitle("Sign out");
		// Log out the user by closing the session. 
		VaadinSession session = VaadinSession.getCurrent();
		if (session.getAttribute("user") != null) {
			User user = RestClient.getUserWithName(
					String.valueOf(session.getAttribute("user"))
			);
			if (user != null) {
				Page.getCurrent().setLocation(
						HOME_URI + LoggedOutLayout.uri(user)
				);
			} else {
				Page.getCurrent().setLocation(HOME_URI);
			}
			VaadinSession.getCurrent().close();
		} else {
			addComponent(new Label(
					"Not signed in."
			));
		}
	}
}
