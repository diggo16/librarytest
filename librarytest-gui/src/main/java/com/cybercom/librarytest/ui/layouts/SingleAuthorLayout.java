package com.cybercom.librarytest.ui.layouts;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.ui.RestClient;
import com.cybercom.librarytest.ui.SecUtils;
import com.cybercom.librarytest.ui.ViewUtils;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class SingleAuthorLayout extends VerticalLayout {
	private static final long serialVersionUID = -9004839388733872159L;
	
	public static final String BASE_URI = "/author/";

	public static String uri(Author author) {
		return HOME_URI + BASE_URI + author.getId();
	}
	
	public SingleAuthorLayout(Long authorId) {
		Author author = RestClient.getAuthor(authorId);
		if (author == null) {
			addComponent(new Label("Invalid author."));
			return;
		}
		Page.getCurrent().setTitle("View author: " + author);
		Image image = ViewUtils.createImageTypewriter();
		Label headerLabel = new Label("View author");
		headerLabel.setId("main-content-header");
		Label nameLabel = new Label(author.getFirstName() + " " + author.getLastName());
		nameLabel.setCaption("Name: ");
		Label countryLabel = new Label(author.getCountry());
		countryLabel.setCaption("Country: ");
		Label bioLabel = new Label(author.getBio());
		bioLabel.setCaption("Biography: ");
		Grid booksGrid = ViewUtils.createBooksGrid(RestClient.getBooksByAuthor(author));
		booksGrid.setCaption("Books: ");
		booksGrid.setHeightByRows(6.0);
		booksGrid.setHeightMode(HeightMode.ROW);
		addComponents(image, headerLabel, nameLabel, 
				countryLabel, bioLabel, booksGrid);
		HorizontalLayout editDelLayout = new HorizontalLayout();
		if (SecUtils.currentUserCan("edit author")) {
			editDelLayout.addComponent(ViewUtils.createEditAuthorButton(author));
		}
		if (SecUtils.currentUserCan("delete author")) {
			editDelLayout.addComponent(ViewUtils.createDeleteEntityButton(author));
		}
		if (editDelLayout.getComponentCount() > 0) {
			editDelLayout.setSpacing(true);
			addComponent(editDelLayout);
		}
	    setMargin(true);
	    setSpacing(true);
	}
}
