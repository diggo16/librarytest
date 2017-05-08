package com.cybercom.librarytest.ui;

import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.model.User.Role;
import com.cybercom.librarytest.ui.PasswordStorage.CannotPerformOperationException;
import com.cybercom.librarytest.ui.PasswordStorage.InvalidHashException;
import com.vaadin.server.VaadinSession;

public abstract class SecUtils {
	
	private static final String[] LIBRARIAN_PRIVILEGES = {
			"view book",
			"view author",
			"view user",
			"add book",
			"add author", 
			"add user",
			"edit book", 
			"edit author",
			"edit user",
			"delete book", 
			"delete author",
			"delete user",
			"borrow book",
			"return book", 
	};
	
	private static final String[] BORROWER_PRIVILEGES = {
			"view book",
			"view author",
			"borrow book",
			"return book"
	};
	
	private static final String[] NOT_SIGNED_IN_PRIVILEGES = {
			"view book",
			"view author", 
			"add user"
	};
	
	/**
	 * Returns true if the current user is allowed to perform all
	 * specified actions, otherwise false.
	 */
	public static boolean currentUserCan(String ...actions) {
		
		// Determine which privileges the user has.
		Object userObj = VaadinSession.getCurrent().getAttribute("user");
		String[] privileges;
		if (userObj == null) {
			privileges = NOT_SIGNED_IN_PRIVILEGES; // not signed in
		} else {
			User user = RestClient.getUserWithName(userObj.toString());
			if (user == null) {
				privileges = NOT_SIGNED_IN_PRIVILEGES; // unknown user
			} else if (user.getRole().equals(Role.LIBRARIAN)) {
				privileges = LIBRARIAN_PRIVILEGES; 
			} else if (user.getRole().equals(Role.LOANER)) {
				privileges = BORROWER_PRIVILEGES;
			} else {
				privileges = NOT_SIGNED_IN_PRIVILEGES; // undefined role
			}
		}
		
		// Check the requested actions against the user's privileges. 
		boolean userCan = true;
		outer:
		for (String action : actions) {
			for (String adminAction : privileges) {
				if (action.equals(adminAction)) {
					continue outer;
				}
			}
			return false; // undefined action
		}
		return userCan;
	}
	
	/**
	 * Returns true if the current user is allowed to view the specified user's profile.
	 */
	public static boolean currentUserCanViewProfile(User user) {
		return currentUserCan("view user") || 
				(user != null && user.getDisplayName().equals(getCurrentUserName()));
	}
	
	/**
	 * Returns true if the current user is allowed to edit the specified user's profile.
	 */
	public static boolean currentUserCanEditProfile(User user) {
		return currentUserCan("edit user") || 
				(user != null && user.getDisplayName().equals(getCurrentUserName()));
	}
	
	/**
	 * Returns true if the current user is allowed to delete the specified user's profile.
	 */
	public static boolean currentUserCanDeleteProfile(User user) {
		return currentUserCan("delete user") || 
				(user != null && user.getDisplayName().equals(getCurrentUserName()));
	}
	
	/**
	 * Returns current user as a User entity from the database, 
	 * or null if user is not logged in.
	 */
	public static User getCurrentUser() {
		return RestClient.getUserWithName(getCurrentUserName());
	}
	
	/**
	 * Returns the name of the current user, or null if user is not logged in.
	 */
	public static String getCurrentUserName() {
		Object userObj = VaadinSession.getCurrent().getAttribute("user"); 
		if (userObj == null || userObj.toString().length() == 0) {
			return null;
		}
		return userObj.toString();
	}
	
	/**
	 * Confirms that the given user credentials match a user in the database, 
	 * and returns that user. Returns null if the credentials don't match.
	 */
	public static User validateCredentials(String name, String pwd) {
		User user = RestClient.getUserWithName(name); 
		if (user == null) {
			return null;
		}
		try {
			return PasswordStorage.verifyPassword(pwd, user.getPassword()) ? 
					user : null;
		} catch (CannotPerformOperationException | InvalidHashException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Signs in the given user. This will save the user's name in the session's
	 * 'user' attribute.
	 * Note that this does not perform any verification of the user credentials.
	 */
	public static void signInUser(User user) {
		VaadinSession.getCurrent().setAttribute("user", user);
	}
}
