package com.cybercom.librarytest.ui.layouts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.ViewUtils;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

/**
 * Base class for Layouts that can persist book data to the database.
 * Contains TextFields for the input and a Button to submit the data.
 * @author Lennart Moraeus
 *
 */
public abstract class PersistBookLayout extends VerticalLayout 
		implements ClickListener {
	private static final long serialVersionUID = -9189102772819938221L;
	
	private Image image;
	protected Label headerLabel;
	private TextField titleInput;
	private TwinColSelect authorsInput;
	private TextArea descriptionInput;
	private TextField isbnInput;
	private TextField nbrPagesInput;
	private TextField datePublishedInput;
	private TextField nbrInInventoryInput;
	protected Button persistBookButton;
	private HorizontalLayout resultLayout;
	private ArrayList<Author> authorsSelected;
	protected Book book;
	
	@SuppressWarnings("unchecked") 
	public PersistBookLayout(Book book) {
		if (!userCanAccess()) {
			return;
		}
		this.book = book == null ? new Book() : book;
		String       titleValue      = book == null ? "" : book.getTitle();
		List<Author> authorsSelValue = book == null ? 
								 new ArrayList<Author>() : book.getAuthors();
		String       descriptValue   = book == null ? "" : book.getDescription();
		String       isbnValue       = book == null ? "" : book.getIsbn();
		Integer      nbrPagesValue   = book == null ? 0  : book.getNbrPages();
		String       datePublValue   = book == null ? 
				                            "YYYY-MM-DD" : book.getPublicationDate();
		Integer 	 nbrInInvValue   = book == null ? 1  : book.getTotalNbrCopies();
		image = ViewUtils.createImageBook();
		headerLabel = new Label();
		headerLabel.setId("main-content-header");
		titleInput = new TextField();
		titleInput.setCaption("Title:");
		titleInput.setRequired(true);
		titleInput.setValue(titleValue);
		authorsInput = new TwinColSelect();
		authorsInput.setLeftColumnCaption("Available authors");
		authorsInput.setRightColumnCaption("Selected authors");
		authorsInput.setNullSelectionAllowed(false);
		authorsSelected = new ArrayList<>();
		authorsInput.addValueChangeListener(
//				e -> // TODO: Find a way to check type of 
//				     //       e.getProperty().getValue() 
//				     //       to avoid unchecked cast warning.
				new ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent e) {
						authorsSelected = new ArrayList<Author>(
								(Collection<Author>) e.getProperty().getValue()
						);	
					}
				}
		);
		authorsInput.addValidator(
				new Validator() {
					private static final long serialVersionUID = 1L;
					@Override
					public void validate(Object o) {
//						if (authorsSelected == null || authorsSelected.size() == 0) {
//							throw new InvalidValueException("Please select at "
//									+ "least one author.");
//						}
					}
				}
		);
		for (Author author : RestClient.getAllAuthors()) {
			authorsInput.addItem(author);
			authorsInput.setItemCaption(author, author.toString());
			if (authorsSelValue.contains(author)) {
				authorsInput.select(author);
			}
		}
		descriptionInput = new TextArea();
		descriptionInput.setCaption("Description:");
		descriptionInput.setMaxLength(2000);
		descriptionInput.setValue(descriptValue);
		isbnInput = new TextField();
		isbnInput.setCaption("ISBN:");
		isbnInput.setMaxLength(20);
		isbnInput.setValue(isbnValue);
		nbrPagesInput = new TextField();
		nbrPagesInput.setCaption("Number of pages:");
		nbrPagesInput.addValidator(
				new Validator() {
					private static final long serialVersionUID = 1L;
					@Override
					public void validate(Object value) { 
						try {
							Integer.parseInt(String.valueOf(value));
						} catch (NumberFormatException e) {
							throw new InvalidValueException("Not an integer.");
						}
					}
				}
		);
		nbrPagesInput.setValue(nbrPagesValue.toString());
		datePublishedInput = new TextField(); // TODO: should be datepicker
		datePublishedInput.setCaption("Date published:");
		datePublishedInput.setValue(datePublValue);
		nbrInInventoryInput = new TextField(); 
		nbrInInventoryInput.setCaption("Number in inventory:");
		nbrInInventoryInput.setValue(nbrInInvValue != 0 ? nbrInInvValue.toString() : "");
	    persistBookButton = new Button("Add author");
	    persistBookButton.addClickListener(this);
	    
	    VerticalLayout nbpIsbnLayout = new VerticalLayout();
	    nbpIsbnLayout.addComponents(nbrPagesInput, isbnInput);
	    nbpIsbnLayout.setSpacing(true);
	    HorizontalLayout descNbpIsbnLayout = new HorizontalLayout();
	    descNbpIsbnLayout.addComponents(descriptionInput, nbpIsbnLayout);
	    descNbpIsbnLayout.setSpacing(true);	    
	    addComponents(
	    		image, headerLabel, 
	    		titleInput, authorsInput, descNbpIsbnLayout, nbrInInventoryInput, 
	    		datePublishedInput, persistBookButton
	    );
	    setMargin(true);
	    setSpacing(true);
	}
	
	/**
	 * Validate fields and persist Book according to methods defined in subclass.
	 */
	@Override
	public void buttonClick(Button.ClickEvent event) {
		if (resultLayout != null) {
			removeComponent(resultLayout);
		}
		resultLayout = new HorizontalLayout();
		
		// Validate input fields. This only fails if the fields are empty.
		try {
			titleInput.validate();
			authorsInput.validate();
			descriptionInput.validate();
			isbnInput.validate();
			nbrPagesInput.validate();
			datePublishedInput.validate();
		} catch (InvalidValueException e) {
			e.printStackTrace();
			resultLayout.addComponent(
					new Label("Invalid data, please try again.")
			);
			addComponent(resultLayout);
			return;
		}
		
		// Create the new book and send it to the REST service.
		Book bookToPersist = new Book(
				book.getId(), // <- can be null
				titleInput.getValue(), 
				descriptionInput.getValue() == "" ? null : descriptionInput.getValue(),
				isbnInput.getValue() == "" ? null : isbnInput.getValue(), 
				Integer.parseInt(nbrPagesInput.getValue()), 
				datePublishedInput.getValue() == "" ? 
						null : datePublishedInput.getValue(), 
				Integer.parseInt(nbrInInventoryInput.getValue())
		);
		bookToPersist.setAuthors(authorsSelected);
		RestClient<Book>.Result result = persistBook(bookToPersist);
		if (result.success) {
			// Success
			if (bookToPersist.getId() == null) {
				bookToPersist.setId(Long.parseLong(result.msg.split("/")[5]));
			}
			resultLayout.addComponents(
					new Label(persistSucceededMessage()), 
					ViewUtils.createBookLink(bookToPersist)
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
	 * of verifying that user is allowed to access the page.
	 */
	protected abstract boolean userCanAccess();
	
	/**
	 * Must be overridden by subclass to determine method
	 * of persisting the book.
	 */
	protected abstract RestClient<Book>.Result persistBook(Book book);
	
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
