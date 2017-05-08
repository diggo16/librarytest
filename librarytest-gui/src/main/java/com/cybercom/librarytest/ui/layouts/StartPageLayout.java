package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.ui.SecUtils;
import com.cybercom.librarytest.ui.ViewUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class StartPageLayout extends VerticalLayout {
	private static final long serialVersionUID = -4170742115837403043L;

	public StartPageLayout() {
		Page.getCurrent().setTitle("Library Test Application");
		addComponent(ViewUtils.createImageBookshelf());
		addComponent(new Label("Welcome to the Library Test Application!"));
		String user = SecUtils.getCurrentUserName();
		if (user != null) {
			addComponent(new Label(
					"Logged in as " + user + "."
			));
		}
		addComponent(new Label("Please choose an action in the left-hand side menu."));
	    setMargin(true);
	    setSpacing(true);
	}
}
