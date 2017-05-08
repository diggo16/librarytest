package com.cybercom.librarytest.ui.layouts;

import com.cybercom.librarytest.model.Authors;
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

public class BrowseAuthorsLayout extends VerticalLayout {
	private static final long serialVersionUID = -9189102772819938221L;

	public static final String BASE_URI = "/authors";
	
	private Label headerLabel;
	private TextField nameInput;
	private TextField countryInput;
	private Button searchButton;
	private Grid resultGrid;
	private Image image;
	
	public BrowseAuthorsLayout() {
		Page.getCurrent().setTitle("Browse authors");
		headerLabel = new Label("Browse authors");
		headerLabel.setId("main-content-header");
		image = ViewUtils.createImageTypewriter();
		nameInput = new TextField();
		nameInput.setCaption("Name:");
		countryInput = new TextField();
		countryInput.setCaption("Country:");
	    searchButton = new Button("Search");
	    searchButton.setId("search-authors-button");
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
	    		image, headerLabel, nameInput, countryInput, searchButton
	    );
	    setMargin(true);
	    setSpacing(true);
	}
	
	private void performSearchAndDisplayResults() {
		// Start with all authors.
		Authors authors = RestClient.getAllAuthors();
		
		// Filter for name.
		if (!nameInput.getValue().isEmpty()) {
			authors = SearchUtils.filterAuthorsForName(nameInput.getValue(), authors);
		}
		
		// Filter for country.
		if (!countryInput.getValue().isEmpty()) {
			authors = SearchUtils.filterAuthorsForCountry(countryInput.getValue(), authors);
		}
		
	    // Add all remaining authors to the Grid and add the Grid to the layout.
		if (authors.size() == 0) {
			return;
		}
		if (resultGrid != null) {
			removeComponent(resultGrid);
		}
		resultGrid = ViewUtils.createAuthorsGrid(authors);
//		resultGrid.setHeightMode(HeightMode.ROW);
//		resultGrid.setHeightByRows(authors.size());
		addComponent(resultGrid);
	}
}
