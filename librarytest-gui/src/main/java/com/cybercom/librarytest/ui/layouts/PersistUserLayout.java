package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.model.User.Role;
import com.cybercom.librarytest.ui.PasswordStorage;
import com.cybercom.librarytest.ui.PasswordStorage.CannotPerformOperationException;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import static com.cybercom.librarytest.ui.MyUI.HOME_URI;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Base class for Layouts that can persist book data to the database.
 * Contains TextFields for the input and a Button to submit the data.
 * @author Lennart Moraeus
 *
 */
public abstract class PersistUserLayout extends VerticalLayout 
		implements Button.ClickListener, ValueChangeListener  {
	private static final long serialVersionUID = -9189102772819938221L;
	
	protected Label headerLabel;
	private TextField displayNameInput;
	private PasswordField passwordInput;
	private TextField firstNameInput;
	private TextField lastNameInput;
	private TextField phoneInput;
	private TextField emailInput;
	protected Button persistUserButton;
	private HorizontalLayout resultLayout;
	protected User user;
	private static final Role DEFAULT_ROLE = Role.LOANER;
	private Role roleSelected; 
	
	public PersistUserLayout(User user) {
		this.user = user == null ? new User() : user;	
		if (!userCanAccess()) {
			return;
		}
		String displayNameValue = user == null ? ""           : user.getDisplayName();
		String passwordValue    = user == null ? ""           : user.getPassword();
		String firstNameValue   = user == null ? ""           : user.getFirstName();
		String lastNameValue    = user == null ? ""           : user.getLastName();
		String phoneValue   	= user == null ? ""           : user.getPhone();
		String emailValue   	= user == null ? ""           : user.getEmail();
		roleSelected 			= user == null ? DEFAULT_ROLE : user.getRole();
		headerLabel = new Label();
		headerLabel.setId("main-content-header");
		displayNameInput = new TextField();
		displayNameInput.setCaption("Display name:");
		displayNameInput.setRequired(true);
		displayNameInput.setValue(displayNameValue);
		passwordInput = new PasswordField();
		passwordInput.setCaption("Password:");
		passwordInput.setRequired(true);
		passwordInput.setValue(passwordValue);
		firstNameInput = new TextField();
		firstNameInput.setCaption("First name:");
		firstNameInput.setValue(firstNameValue == null ? "" : firstNameValue);
		lastNameInput = new TextField();
		lastNameInput.setCaption("Last name:");
		lastNameInput.setValue(lastNameValue == null ? "" : lastNameValue);
		phoneInput = new TextField();
		phoneInput.setCaption("Phone:");
		phoneInput.setValue(phoneValue == null ? "" : phoneValue);
		emailInput = new TextField();
		emailInput.setCaption("Email:");
		emailInput.setValue(emailValue == null ? "" : emailValue);
	    persistUserButton = new Button("Add user");
	    persistUserButton.addClickListener(this);
	    GridLayout outerLayout = new GridLayout(2, 1);
		outerLayout.addComponents(displayNameInput, passwordInput);
		outerLayout.newLine();
		outerLayout.addComponents(firstNameInput, lastNameInput);
		outerLayout.newLine();
		outerLayout.addComponents(phoneInput, emailInput);
		if (SecUtils.currentUserCan("edit user")) {
	    	OptionGroup roleInput = new OptionGroup("Role:");
	    	roleInput.addItems((Object[])Role.values());
	    	roleInput.select(roleSelected);
	    	roleInput.addValueChangeListener(this);
	    	outerLayout.newLine();
	    	outerLayout.addComponent(roleInput);
	    }
		outerLayout.setSpacing(true);
		addComponents(headerLabel, outerLayout, persistUserButton);
	    setMargin(true);
	    setSpacing(true);
	}
	
	/**
	 * Validate fields and persist Book according to methods defined in subclass.
	 */
	public void buttonClick(ClickEvent event) {
		if (resultLayout != null) {
			removeComponent(resultLayout);
		}
		resultLayout = new HorizontalLayout();
		
		// Validate input fields. This only fails if the fields are empty.
		try {
			displayNameInput.validate();
			passwordInput.validate();
			firstNameInput.validate();
			lastNameInput.validate();
			phoneInput.validate();
			emailInput.validate();
		} catch (InvalidValueException e) {
			e.printStackTrace();
			resultLayout.addComponent(
					new Label("Invalid data, please try again.")
			);
			addComponent(resultLayout);
			return;
		}
		
		// Create a hash of the password to store in the database.
		String pwdHash;
		try {
			pwdHash = PasswordStorage.createHash(passwordInput.getValue());
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
			resultLayout.addComponent(
					new Label("Invalid password, please try again.")
			);
			addComponent(resultLayout);
			return;
		}
		
		// Create the new user and send it to the REST service.
		User userToPersist = new User(
				user.getId(), // <- can be null
				displayNameInput.getValue() == "" ? null : displayNameInput.getValue(),  
				pwdHash, 
				roleSelected, 
				firstNameInput.getValue() == "" ? null : firstNameInput.getValue(), 
				lastNameInput.getValue() == "" ? null : lastNameInput.getValue(), 
				phoneInput.getValue() == "" ? null : phoneInput.getValue(), 
				emailInput.getValue() == "" ? null : emailInput.getValue()
		);
		RestClient<User>.Result result = persistUser(userToPersist);
		if (result.success) {
			// Success
			
			// Admins should get a link to the new user.
			// Normal users should be taken to the login screen after creating 
			//    an account.
			if (SecUtils.currentUserCan("edit user")) {
				if (userToPersist.getId() == null) {
					userToPersist.setId(Long.parseLong(result.msg.split("/")[5]));
				}
				resultLayout.addComponents(
						new Label(persistSucceededMessage())/*, 
						ViewUtils.createUserLink(userToPersist)*/ // Viewing other users'
																  // profiles is not
				);												  // supported.
			} else if (userToPersist.getId() == null) {
				Page.getCurrent().setLocation(HOME_URI + LoginLayout.BASE_URI);
			}
			
		} else {
			// Failure
			resultLayout.addComponent(
					new Label(persistFailedMessage() + result)
			);
		}
		addComponent(resultLayout);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		roleSelected = strToRole(event.getProperty().getValue().toString());
	}
	
	/**
	 * Hack solution for converting String into Role, 
	 * necessary because Vaadin OptionGroup is not generic. 
	 */
	private static Role strToRole(String roleStr) {
		try {
			return Role.valueOf(roleStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Must be overridden by subclass to determine method
	 * of verifying that user is allowed to access the page.
	 */
	protected abstract boolean userCanAccess();
	
	/**
	 * Must be overridden by subclass to determine method
	 * of persisting the user.
	 */
	protected abstract RestClient<User>.Result persistUser(User user);
	
	/**
	 * Must be overridden by subclass to determine message displayed 
	 * when persisting fails.
	 */
	protected abstract String persistFailedMessage();

	/**
	 * Must be overridden by subclass to determine message displayed 
	 * when persisting succeeds.
	 */
	protected abstract String persistSucceededMessage();
}
