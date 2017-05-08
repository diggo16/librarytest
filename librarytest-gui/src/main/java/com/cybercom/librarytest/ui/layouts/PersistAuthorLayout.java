package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.ViewUtils;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Base class for Layouts that can persist author data to the database.
 * Contains TextFields for the input and a Button to submit the data.
 * @author Lennart Moraeus
 *
 */
public abstract class PersistAuthorLayout extends VerticalLayout
		implements ClickListener {
	private static final long serialVersionUID = -9189102772819938221L;
	
	private Image image;
	protected Label headerLabel;
	protected TextField firstNameInput;
	protected TextField lastNameInput;
	protected TextField countryInput;
	protected TextArea bioInput;
	protected Button persistAuthorButton;
	protected HorizontalLayout resultLayout;
	private String firstNameValue;
	private String lastNameValue;
	private String countryValue;
	private String bioValue;
	protected Author author;
	
	public PersistAuthorLayout(Author author) {
		this.author = author == null ? new Author() : author;
		firstNameValue = author == null ? "" : author.getFirstName();
		lastNameValue  = author == null ? "" : author.getLastName();
		countryValue   = author == null ? "" : author.getCountry();
		bioValue       = author == null ? "" : author.getBio();
		image = ViewUtils.createImageTypewriter();
		headerLabel = new Label();
		headerLabel.setId("main-content-header");
		firstNameInput = new TextField();
		firstNameInput.setCaption("First name:");
		firstNameInput.setRequired(true);
		firstNameInput.setValue(firstNameValue);
		lastNameInput = new TextField();
		lastNameInput.setCaption("Last name:");
		lastNameInput.setRequired(true);
		lastNameInput.setValue(lastNameValue);
		HorizontalLayout nameLayout = new HorizontalLayout();
		nameLayout.addComponents(firstNameInput, lastNameInput);
		nameLayout.setSpacing(true);
		countryInput = new TextField();
		countryInput.setCaption("Country:");
		countryInput.setRequired(true);
		countryInput.setValue(countryValue);
		bioInput = new TextArea();
		bioInput.setCaption("Biography:");
		bioInput.setRequired(true);
		bioInput.setMaxLength(2000);
		bioInput.setValue(bioValue);
		bioInput.setWidth("380px");
	    persistAuthorButton = new Button("Add author");
	    persistAuthorButton.addClickListener(this);
	    addComponents(
	    		image, headerLabel, 
	    		nameLayout, countryInput, 
	    		bioInput, persistAuthorButton
	    );
	    setMargin(true);
	    setSpacing(true);
	}
	
	@Override
	public void buttonClick(Button.ClickEvent event) {
		if (resultLayout != null) {
			removeComponent(resultLayout);
		}
		resultLayout = new HorizontalLayout();
		
		// Validate input fields. This only fails if the fields are empty.
		try {
			firstNameInput.validate();
			lastNameInput.validate();
			countryInput.validate();
			bioInput.validate();
		} catch (InvalidValueException e) {
			e.printStackTrace();
			resultLayout.addComponent(
					new Label("Invalid data, please try again.")
			);
			addComponent(resultLayout);
			return;
		}
		
		// Create the new author and send it to the REST service.
		Author authorToPersist = new Author(
				this.author.getId(),
				firstNameInput.getValue(), lastNameInput.getValue(),
				countryInput.getValue(), bioInput.getValue()
		);
		RestClient<Author>.Result result = persistAuthor(authorToPersist);
		if (result.success) {
			// Success
			if (authorToPersist.getId() == null) {
				authorToPersist.setId(Long.parseLong(result.msg.split("/")[5]));
			}
			resultLayout.addComponents(
					new Label(persistSucceededMessage()), 
					ViewUtils.createAuthorLink(authorToPersist)
			);
		} else {
			// Failure
			resultLayout.addComponent(
					new Label(persistFailedMessage() + result)
			);
		}
		addComponent(resultLayout);
	}
	
	/**
	 * Must be overridden by subclass to determine method
	 * of persisting the author.
	 */
	protected abstract RestClient<Author>.Result persistAuthor(Author author);
	
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
