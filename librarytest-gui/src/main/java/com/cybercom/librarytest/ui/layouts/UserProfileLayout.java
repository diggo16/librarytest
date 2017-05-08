package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.cybercom.librarytest.ui.ViewUtils;
import static com.cybercom.librarytest.ui.MyUI.HOME_URI;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Layout for viewing a user's profile.
 * Contains Labels for all the fields and a Grid for the user's loans.
 * If the current user does not have privilege "view users", they are only allowed
 * to view their own user profile. 
 * @author Lennart Moraeus
 *
 */
public class UserProfileLayout extends VerticalLayout {
	private static final long serialVersionUID = -9004839388733872159L;
	public static final String BASE_URI = "/user/profile/";

	public static String uri(User user) {
		return HOME_URI + BASE_URI + user.getId();
	}
	
	public UserProfileLayout() {
		this(RestClient.getUserWithName(SecUtils.getCurrentUserName()));
	}
	
	public UserProfileLayout(String userName) {
		this(RestClient.getUserWithName(userName));
	}
	
	public UserProfileLayout(Long id) {
		this(RestClient.getUserWithId(id));
	}
	
	public UserProfileLayout(User user) {
		if (user == null || !SecUtils.currentUserCanViewProfile(user)) {
			addComponent(new Label("You lack permission to view this page."));
			return;
		}
		Page.getCurrent().setTitle("User profile");
		Label headerLabel = new Label("User profile");
		headerLabel.setId("main-content-header");
		Label displayNameLabel = new Label(user.getDisplayName());
		displayNameLabel.setCaption("Display name: ");
		HorizontalLayout idRoleLayout = new HorizontalLayout();
		Label idLabel = null;
		Label roleLabel = null;
		if (SecUtils.currentUserCan("view profile")) {
			idLabel = new Label(user.getId().toString());
			idLabel.setCaption("User ID: ");
			roleLabel = new Label(user.getRole().toString());
			roleLabel.setCaption("Role: ");
			idRoleLayout.addComponents(idLabel, roleLabel);
		}
		HorizontalLayout firstLastNameLayout = new HorizontalLayout();
		Label firstNameLabel = new Label(user.getFirstName());
		firstNameLabel.setCaption("First name: ");
		Label lastNameLabel = new Label(user.getLastName());
		lastNameLabel.setCaption("Last name: ");
		firstLastNameLayout.addComponents(firstNameLabel, lastNameLabel);
		HorizontalLayout phoneEmailLayout = new HorizontalLayout();
		Label phoneLabel = new Label(user.getPhone());
		phoneLabel.setCaption("Phone: ");
		Label emailLabel = new Label(user.getEmail());
		emailLabel.setCaption("Email: ");
		phoneEmailLayout.addComponents(phoneLabel, emailLabel);
		Grid loansGrid = ViewUtils.createUserLoansGrid(RestClient.getAllLoansOfUser(user));
		loansGrid.setCaption("Loans: ");
		loansGrid.setHeightByRows(6.0);
		loansGrid.setHeightMode(HeightMode.ROW);
		GridLayout outerLayout = new GridLayout(2, 1);
		outerLayout.addComponent(displayNameLabel);
		outerLayout.newLine();
		if (SecUtils.currentUserCan("view profile")) {
			outerLayout.addComponent(idLabel);
			outerLayout.addComponent(roleLabel);
			outerLayout.newLine();
		}
		outerLayout.addComponent(firstNameLabel);
		outerLayout.addComponent(lastNameLabel);
		outerLayout.newLine();
		outerLayout.addComponent(phoneLabel);
		outerLayout.addComponent(emailLabel);
		outerLayout.setSpacing(true);
		addComponents(headerLabel, outerLayout);
		HorizontalLayout editDelLayout = new HorizontalLayout();
		if (SecUtils.currentUserCan("edit user") || 
				SecUtils.currentUserCanEditProfile(user)) {
			editDelLayout.addComponent(ViewUtils.createEditUserButton(user));
		}
		if (SecUtils.currentUserCan("delete user")) {
			editDelLayout.addComponent(ViewUtils.createDeleteEntityButton(user));
		}
		if (editDelLayout.getComponentCount() > 0) {
			editDelLayout.setSpacing(true);
			addComponent(editDelLayout);
		}
		addComponent(loansGrid);
	    setMargin(true);
	    setSpacing(true);
	}
}
