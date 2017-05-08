package com.cybercom.librarytest.ui;

import static com.cybercom.librarytest.ui.MyUI.HOME_URI;

import java.io.File;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.cybercom.librarytest.model.Author;
import com.cybercom.librarytest.model.Authors;
import com.cybercom.librarytest.model.BaseEntity;
import com.cybercom.librarytest.model.Book;
import com.cybercom.librarytest.model.Books;
import com.cybercom.librarytest.model.Loan;
import com.cybercom.librarytest.model.Loans;
import com.cybercom.librarytest.model.User;
import com.cybercom.librarytest.ui.layouts.EditAuthorLayout;
import com.cybercom.librarytest.ui.layouts.EditBookLayout;
import com.cybercom.librarytest.ui.layouts.EditUserLayout;
import com.cybercom.librarytest.ui.layouts.SingleAuthorLayout;
import com.cybercom.librarytest.ui.layouts.SingleBookLayout;
import com.cybercom.librarytest.ui.layouts.UserProfileLayout;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.HtmlRenderer;

public abstract class ViewUtils {
	
	/**
	 * Returns an HTML String with the author as a link. 
	 */
	public static String createAuthorStr(Author author) {
		return "<a href='" + SingleAuthorLayout.uri(author) + 
				"'>" + author + "</a>";
	}
	
	/**
	 * Returns an HTML String with the book as a link. 
	 */
	public static String createBookStr(Book book) {
		return "<a href='" + SingleBookLayout.uri(book) + 
				"'>" + book + "</a>";
	}
	
	/**
	 * Returns a Link representation of a book.
	 */
	public static Link createBookLink(Book book) {
		return new Link(
				book.getTitle(), 
				new ExternalResource(
						SingleBookLayout.uri(book)
				)
		);
	}
	
	/**
	 * Returns a Link representation of an author.
	 */
	public static Link createAuthorLink(Author author) {
		return new Link(
				author.getFirstName() + " " + author.getLastName(), 
				new ExternalResource(
						SingleAuthorLayout.uri(author)
				)
		);
	}
	
	/**
	 * Returns a Link representation of a user.
	 */
	public static Link createUserLink(User user) {
		return new Link(
				user.getDisplayName(), 
				new ExternalResource(
						UserProfileLayout.uri(user)
				)
		);
	}
	
	/**
	 * Returns a HorizontalLayout containing Links of the authors
	 * separated by comma-filled Labels. Includes a caption.
	 */
	public static HorizontalLayout createAuthorsLinkLayout(List<Author> authors) {
		HorizontalLayout authorsLayout = new HorizontalLayout();
		if (authors != null && authors.size() > 0) {
			Label commaBetweenAuthors = null;
			for (Author author : authors) {
				authorsLayout.addComponent(ViewUtils.createAuthorLink(author));
				commaBetweenAuthors = new Label(", ");
				authorsLayout.addComponent(commaBetweenAuthors);
			}
			authorsLayout.removeComponent(commaBetweenAuthors);
			authorsLayout.setCaption(
					authors.size() == 1 ? "Author: " : "Authors: "
			);
		}
		return authorsLayout;
	}
	
	/**
	 * Returns an HTML String with each author as its own link separated by commas. 
	 */
	public static String createAuthorsStr(List<Author> authors) {
		String authorsStr = "";
		for (Author author : authors) {
			authorsStr += createAuthorStr(author);
		}
		authorsStr = authorsStr.replace("</a><a", "</a>, <a");
		return authorsStr;
	}
	
	/**
	 * Returns a Grid with the authors represented as rows.
	 */
	public static Grid createAuthorsGrid(Authors authors) {
		Grid grid = new Grid();
		grid.addColumn("Name", String.class)
				.setRenderer(new HtmlRenderer());
		grid.addColumn("Country", String.class);
		for (Author author : authors) {
			grid.addRow(
					createAuthorStr(author),  
					author.getCountry()
			);
		}
		return grid;
	}

	/**
	 * Returns a Grid with the books represented as rows.
	 */
	public static Grid createBooksGrid(Books books) {
	    Grid grid = new Grid();
	    grid.addColumn("Title", String.class)
				.setRenderer(new HtmlRenderer());
	    grid.addColumn("Authors", String.class)
				.setRenderer(new HtmlRenderer());
	    grid.addColumn("Date published", String.class);
	    grid.addColumn("ISBN", String.class);
	    for (Book book : books) {
			grid.addRow(
					createBookStr(book), 
					createAuthorsStr(book.getAuthors()), 
					book.getPublicationDate(), 
					book.getIsbn()
			);
		}
	    return grid;
	}
	
	/**
	 * Returns a Grid with the loans represented as rows.
	 */
	public static Grid createUserLoansGrid(Loans loans) {
	    Grid grid = new Grid();
	    grid.addColumn("Book title", String.class)
				.setRenderer(new HtmlRenderer());
	    grid.addColumn("Date borrowed", String.class);
	    grid.addColumn("Date due", String.class);
	    loans = loans == null ? new Loans() : loans;
	    for (Loan loan : loans) {
			grid.addRow(
					createBookStr(loan.getBook()), 
					loan.getDateBorrowed(), 
					loan.getDateDue()
			);
		}
	    return grid;
	}
	
	/**
	 * Returns a Button with text "Borrow book" that loans the specified book
	 * to the currently logged in user, if the user answers "Yes" using the 
	 * confirm dialog.
	 */
	public static Button createBorrowBookButton(final Book book) {
		Button borrowBookButton = new Button("Borrow book");
		borrowBookButton.setId("borrow-book-button");
		borrowBookButton.addClickListener(
				new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(Button.ClickEvent e) {
						ConfirmDialog.Listener dialogListener = 
								new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(ConfirmDialog dialog) { 
				                if (dialog.isConfirmed()) {
				                	Loan loan = new Loan(
				                			book, SecUtils.getCurrentUser(), 
				                			"2016-05-06", "2016-05-07"
				                	);
				                	RestClient<Loan>.Result result = 
				                			new RestClient<Loan>().createEntity(loan);
				                	if (result.success) {
				                		MyUI.getCurrentUI().getPage()
				                				.setLocation(SingleBookLayout.uri(book));
				                	} else {
				                		Notification.show(
				                				"Error", 
				                				result.msg, 
				                				Notification.Type.ERROR_MESSAGE
				                		);
				                	}
				                }
							}
						};
						ConfirmDialog.show(
								MyUI.getCurrentUI(), 
								"Please confirm:", 
								"Are you sure you want to borrow book: " + 
										book + "?",
								"Yes", "No", dialogListener 
						);
					}
				}
		);
		return borrowBookButton;
	}
	
	/**
	 * Returns a Button with text "Return book" that returns the specified book
	 * which is borrowed by the currently logged in user, if the user answers "Yes" 
	 * using the confirm dialog.
	 */
	public static Button createReturnBookButton(final Book book) {
		Button returnBookButton = new Button("Return book");
		returnBookButton.setId("return-book-button");
		returnBookButton.addClickListener(
				new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(Button.ClickEvent e) {
						ConfirmDialog.Listener dialogListener = 
								new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(ConfirmDialog dialog) { 
				                if (dialog.isConfirmed()) {
				                	Loan loan = RestClient.getLoanOfUserAndBook(
				                			SecUtils.getCurrentUser(), book
				                	);
				                	RestClient<Loan>.Result result = 
				                			new RestClient<Loan>().deleteEntity(loan);
				                	if (result.success) {
				                		MyUI.getCurrentUI().getPage()
				                				.setLocation(SingleBookLayout.uri(book));
				                	} else {
				                		Notification.show(
				                				"Error", 
				                				result.msg, 
				                				Notification.Type.ERROR_MESSAGE
				                		);
				                	}
				                }
							}
						};
						ConfirmDialog.show(
								MyUI.getCurrentUI(), 
								"Please confirm:", 
								"Are you sure you want to return book: " + 
										book + "?",
								"Yes", "No", dialogListener 
						);
					}
				}
		);
		return returnBookButton;
	}
	
	/**
	 * Returns a Button with text "Edit book", that takes the user to the edit page of
	 * the specified book.
	 */
	public static Button createEditBookButton(final Book book) {
		Button editBookButton = new Button("Edit book");
		editBookButton.setId("edit-book-button");
		editBookButton.addClickListener(
				new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(Button.ClickEvent e) {
						MyUI.getCurrentUI().getPage().setLocation(
								EditBookLayout.uri(book)
						);
					}
				}
		);
		return editBookButton;
	}
	
	/**
	 * Returns a Button with text "Edit author", that takes the user to the edit page of
	 * the specified author.
	 */
	public static Button createEditAuthorButton(final Author author) {
		Button editAuthorButton = new Button("Edit author");
		editAuthorButton.setId("edit-author-button");
		editAuthorButton.addClickListener(
				new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(Button.ClickEvent e) {
						MyUI.getCurrentUI().getPage().setLocation(
								EditAuthorLayout.uri(author)
						);
					}
				}
		);
		return editAuthorButton;
	}
	
	/**
	 * Returns a Button with text "Edit user", that takes the user to the edit page of
	 * the specified user.
	 */
	public static Button createEditUserButton(final User user) {
		Button editUserButton = new Button("Edit user");
		editUserButton.setId("edit-user");
		editUserButton.addClickListener(
				new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(Button.ClickEvent e) {
						MyUI.getCurrentUI().getPage().setLocation(
								EditUserLayout.uri(user)
						);
					}
				}
		);
		return editUserButton;
	}
	
	/**
	 * Returns a Button with text "Delete <entity name>" that deletes the 
	 * specified entity if the user answers "Yes" using the confirm dialog.
	 */
	public static Button createDeleteEntityButton(final BaseEntity entity) {
		Button deleteEntityButton = new Button("Delete " + getEntityText(entity));
		deleteEntityButton.setId("delete-" + getEntityText(entity) + "-button");
		deleteEntityButton.addClickListener(
				new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(Button.ClickEvent e) {
						ConfirmDialog.Listener dialogListener = 
								new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(ConfirmDialog dialog) { 
				                if (dialog.isConfirmed()) {
				                	RestClient<BaseEntity>.Result result = 
				                			new RestClient<>().deleteEntity(entity); 
				                	if (result.success) {
				                		MyUI.getCurrentUI().getPage().setLocation(HOME_URI);
				                	} else {
				                		Notification.show(
				                				"Error", 
				                				result.msg, 
				                				Notification.Type.ERROR_MESSAGE
				                		);
				                	}
				                }
							}
						};
						ConfirmDialog.show(
								MyUI.getCurrentUI(), 
								"Please confirm:", 
								"Are you sure you want to delete " + 
										getEntityText(entity) + ": " + 
										entity + "?",
								"Yes", "No", dialogListener 
						);
					}
				}
		);
		return deleteEntityButton;
	}
	
	private static String getEntityText(BaseEntity entity) {
		if (entity instanceof Book) {
			return "book";
		} else if (entity instanceof Author) {
			return "author";
		} else if (entity instanceof User) {
			return "user";
		}
		return null;
	}
	
	private static final String IMAGE_WIDTH = "400px";
	private static final String IMAGE_HEIGHT = "267px";
	
	public static Image createImageTypewriter() {
		Image image = createImage("typewriter-1580800-1278x855.jpg");
		return image;
	}
	
	public static Image createImageBookshelf() {
		Image image = createImage("books-4-1421569-1279x852.jpg");
		return image;
	}
	
	public static Image createImageBooks() {
		Image image = createImage("old-books-1520670-1279x856.jpg");
		return image;
	}
	
	public static Image createImageBook() {
		Image image = createImage("soldier-s-prayer-book-in-1917-1553594-1280x960.jpg");
		return image;
	}
	
	private static Image createImage(String fileName) {
		String basepath = VaadinService.getCurrent()
				.getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath + 
				"/WEB-INF/images/" + fileName));
		Image image = new Image(null, resource); 
		image.setWidth(IMAGE_WIDTH);
		image.setHeight(IMAGE_HEIGHT);
		return image;
	}
}
