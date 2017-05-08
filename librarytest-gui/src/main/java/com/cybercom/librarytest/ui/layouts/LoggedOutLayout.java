package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.ui.RestClient;
import static com.cybercom.librarytest.ui.MyUI.HOME_URI;
import com.vaadin.server.Page;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Layout to navigate to after logging out a user.
 */
public class LoggedOutLayout extends VerticalLayout {
	private static final long serialVersionUID = -2939244122020841212L;

	public static final String BASE_URI = "/user/loggedout/";
	public static String uri(User user) {
		return HOME_URI + BASE_URI + user.getId();
	}
	
	public LoggedOutLayout(Long userId) {
		Page.getCurrent().setTitle("Signed out");
		User user = RestClient.getUserWithId(userId);
		addComponent(new Label(
				"Signed out user " + String.valueOf(user) + "."
		));
	}
}
