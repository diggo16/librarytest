package com.cybercom.librarytest.ui.layouts;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.Label;

public class EditAuthorLayout extends PersistAuthorLayout {
	private static final long serialVersionUID = -9189102772819938221L;
	
	private static final RestClient<Author> REST_CLIENT = new RestClient<>();
	
	public static final String BASE_URI = "/edit/author/";
	
	public static String uri(Author author) {
		return HOME_URI + BASE_URI + author.getId();
	}
	
	public EditAuthorLayout(Long authorId) {
		super(RestClient.getAuthor(authorId));
		if (!SecUtils.currentUserCan("edit author") || 
				author == null || author.getId() == null) {
			removeAllComponents();
			addComponent(new Label("Invalid author."));
			return;
		}
		Page.getCurrent().setTitle("Edit author");
		headerLabel.setValue("Edit author");
		persistAuthorButton.setCaption("Save changes");
		persistAuthorButton.setId("save-author-button");
	}

	@Override
	protected RestClient<Author>.Result persistAuthor(Author author) {
		return REST_CLIENT.updateEntity(author);
	}
	
	@Override
	protected String persistFailedMessage() {
		return "Unable to save changes to author: ";
	}

	@Override
	protected String persistSucceededMessage() {
		return "Saved changes to author: ";
	}
}
