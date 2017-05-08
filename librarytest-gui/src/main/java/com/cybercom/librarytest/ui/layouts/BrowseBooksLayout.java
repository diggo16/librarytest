package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.Books;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SearchUtils;
import com.cybercom.librarytest.ui.ViewUtils;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BrowseBooksLayout extends VerticalLayout {
	private static final long serialVersionUID = -9189102772819938221L;

	public static final String BASE_URI = "/books";
	
	private Image image;
	private Label headerLabel;
	private TextField titleInput;
	private TextField authorNameInput;
	private TextField isbnInput;
	private TextField datePublishedInput;
	private Button searchButton;
	private Grid resultGrid;
	
	public BrowseBooksLayout() {
		Page.getCurrent().setTitle("Browse books");
		image = ViewUtils.createImageBooks();
		headerLabel = new Label("Browse books");
		headerLabel.setId("main-content-header");
		titleInput = new TextField();
		titleInput.setCaption("Title:");
		authorNameInput = new TextField();
		authorNameInput.setCaption("Name of author:");
		isbnInput = new TextField();
		isbnInput.setCaption("ISBN:");		
		datePublishedInput = new TextField();
		datePublishedInput.setCaption("Date published:");
	    searchButton = new Button("Search");
	    searchButton.setId("search-books-button");
	    searchButton.addClickListener(
	    		new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
	    			public void buttonClick(Button.ClickEvent e) { 
	    				performSearchAndDisplayResults();
	    			}
	    		}
	    );
	    addComponents(
	    		image, headerLabel, titleInput, 
	    		authorNameInput, isbnInput, datePublishedInput, 
	    		searchButton
	    );
	    setMargin(true);
	    setSpacing(true);
	}
	
	private void performSearchAndDisplayResults() {
		// Start with all books.
		Books books = RestClient.getAllBooks();
		
		// Filter for title.
		if (!titleInput.getValue().isEmpty()) {
			books = SearchUtils.filterBooksForTitle(titleInput.getValue(), books);
		}
		
		// Filter for author name.
		if (!authorNameInput.getValue().isEmpty()) {
			books = SearchUtils.filterBooksForAuthorName(authorNameInput.getValue(), books);
		}
		
		// Filter for isbn.
		if (!isbnInput.getValue().isEmpty()) {
			books = SearchUtils.filterBooksForIsbn(isbnInput.getValue(), books);
		}
		
		// Filter for date published.
		if (!datePublishedInput.getValue().isEmpty()) {
			books = SearchUtils.filterBooksForDatePublished(
					datePublishedInput.getValue(), 
					books
			);
		}

	    // Add all remaining books to the Grid.
		if (books.size() == 0) {
			return;
		}
		if (resultGrid != null) {
			removeComponent(resultGrid);
		}
		resultGrid = ViewUtils.createBooksGrid(books);
		//resultGrid.setSizeFull();
//		resultGrid.setHeightMode(HeightMode.ROW);
//		resultGrid.setHeightByRows(books.size());
		addComponent(resultGrid);
	}
}
