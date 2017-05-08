package com.cybercom.librarytest.ui.layouts;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.Label;

public class EditUserLayout extends PersistUserLayout {
	private static final long serialVersionUID = -9189102772819938221L;
	 
	public static final String BASE_URI = "/edit/user/";
	
	public static String uri(User user) {
		return HOME_URI + BASE_URI + user.getId();
	}
	
	public EditUserLayout(Long userId) {
		super(RestClient.getUserWithId(userId));
		if (!userCanAccess() || user == null || user.getId() == null) {
			removeAllComponents();
			addComponent(new Label("Invalid user."));
			return;
		}
		Page.getCurrent().setTitle("Edit user");
		headerLabel.setValue("Edit user");
	    persistUserButton.setCaption("Save user");
	    persistUserButton.setId("save-user-button");
	}
	
	@Override
	protected boolean userCanAccess() {
		return SecUtils.currentUserCanEditProfile(user);
	}
	
	@Override
	protected RestClient<User>.Result persistUser(User user) {
		return new RestClient<User>().updateEntity(user);
	}

	@Override
	protected String persistFailedMessage() {
		return "Unable to save changes to user: ";
	}

	@Override
	protected String persistSucceededMessage() {
		return "Updated user: ";
	}
}
