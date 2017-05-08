package com.cybercom.librarytest.ui.layouts;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class LoginLayout extends VerticalLayout 
		implements Button.ClickListener {
	private static final long serialVersionUID = -470154707804727323L;
	
	public static final String BASE_URI = "/user/login";
	
	private TextField userInput;
	private PasswordField pwdInput;
	private Button loginButton;
	private VerticalLayout errorMsgLayout;
	private Label userErrorLabel;
	private Label pwdErrorLabel;
	
	public LoginLayout() {
		String user = SecUtils.getCurrentUserName();
		if (user != null) {
			addComponent(new Label(
					"Signed in as " + user + "."
			));
		}
		Page.getCurrent().setTitle("Sign in");
		userInput = new TextField();
		userInput.setCaption("User name:");
		userInput.setId("input-username");
		userInput.setRequired(true);
		userInput.focus();
		pwdInput = new PasswordField();
		pwdInput.setCaption("Password:");
		pwdInput.setId("input-password");
		pwdInput.setRequired(true);
		pwdInput.setNullRepresentation("");
		loginButton = new Button("Submit");
		loginButton.setId("login-button");
		loginButton.addClickListener(this);
		errorMsgLayout = new VerticalLayout();
	    
	    addComponents(
	    		userInput, pwdInput, loginButton, 
	    		errorMsgLayout 
	    );
	    setMargin(true);
	    setSpacing(true);
	}

	@Override
	public void buttonClick(ClickEvent event) {
        
		// Validate user input.
		// This does not check the user credentials against the user 
		// info in the database.
		errorMsgLayout.removeAllComponents();
		try {
			userInput.validate();
		} catch (InvalidValueException e) {
			userErrorLabel = 
					new Label("Error: " + e.getLocalizedMessage());
			errorMsgLayout.addComponent(userErrorLabel);
			return;
		}
		try {
			pwdInput.validate();
		} catch (InvalidValueException e) {
			pwdErrorLabel = 
					new Label("Error: " + e.getLocalizedMessage());
			errorMsgLayout.addComponent(pwdErrorLabel);
		}
		
		// Verify the user credentials against the user info in the database.
		User user = 
				SecUtils.validateCredentials(userInput.getValue(), pwdInput.getValue());
        if (user != null) {
            // Store the current user in the service session
        	SecUtils.signInUser(user);

            // Navigate to main view
            getUI().getPage().setLocation(HOME_URI);
        } else {
            // Wrong password: clear the password field and refocus it
            pwdInput.setValue(null);
            pwdInput.focus();
            pwdErrorLabel = 
					new Label("Error: Wrong password.");
            errorMsgLayout.addComponent(pwdErrorLabel);
        }
	}
}
