package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.vaadin.server.Page;

public class CreateAuthorLayout extends PersistAuthorLayout {
	private static final long serialVersionUID = -9189102772819938221L;
	
	public static final String BASE_URI = "/add/author";
	
	public CreateAuthorLayout() {
		super(null);
		if (!SecUtils.currentUserCan("add author")) {
			return;
		}
		Page.getCurrent().setTitle("Add author");
		headerLabel.setValue("Add author");
	    persistAuthorButton.setDescription("Add author");
	    persistAuthorButton.setId("add-author-button");
	}
	
	@Override
	protected RestClient<Author>.Result persistAuthor(Author author) {
		return new RestClient<Author>().createEntity(author);
	}

	@Override
	protected String persistFailedMessage() {
		return "Unable to add author: ";
	}

	@Override
	protected String persistSucceededMessage() {
		return "Added author: ";
	}
}
