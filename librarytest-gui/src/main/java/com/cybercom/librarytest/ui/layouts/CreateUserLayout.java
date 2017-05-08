package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.server.Page;

public class CreateUserLayout extends PersistUserLayout {
	private static final long serialVersionUID = -9189102772819938221L;
	 
	public static final String BASE_URI = "/add/user";
	
	public CreateUserLayout() {
		super(null);
		if (!userCanAccess()) {
			return;
		}
		Page.getCurrent().setTitle("Add user");
		headerLabel.setValue("Add user");
	    persistUserButton.setDescription("Add user");
	    persistUserButton.setId("add-user-button");
	}
	
	@Override
	protected boolean userCanAccess() {
		return SecUtils.currentUserCan("add user");
	}
	
	@Override
	protected RestClient<User>.Result persistUser(User user) {
		return new RestClient<User>().createEntity(user);
	}

	@Override
	protected String persistFailedMessage() {
		return "Unable to add user: ";
	}

	@Override
	protected String persistSucceededMessage() {
		return "Added user: ";
	}
}
