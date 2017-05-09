/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.librarytest.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author testautomatisering
 */
public class BookPage extends MenuPage{

    @FindBy(css = "#gwt-uid-3")
    SelenideElement titleField;
    @FindBy(css = "#gwt-uid-5")
    SelenideElement authorField;
    @FindBy(css = "#gwt-uid-7")
    SelenideElement descriptionField;
    @FindBy(css = "#gwt-uid-9")
    SelenideElement isbnField;
    @FindBy(css = "#gwt-uid-11")
    SelenideElement datePublishedField;
    @FindBy(css = "#gwt-uid-13")
    SelenideElement nbrAvailableField;
    
    @FindBy(css = "#edit-book-button")
    SelenideElement editBookButton;
    @FindBy(css = "#borrow-book-button")
    SelenideElement borrowBookButton;
    @FindBy(css = "#return-book-button")
    SelenideElement returnBookButton;
    

    public String getTitle() {
        return getTextFieldValue("Title field", titleField);
    }

    public String getAuthor() {
        return getTextFieldValue("Author field", authorField);
    }

    public String getDescription() {
        return getTextFieldValue("description field", descriptionField);
    }

    public String getIsbn() {
        return getTextFieldValue("isbn field", isbnField);
    }
    
    public String getDatePublished() {
        return getTextFieldValue("date published field", datePublishedField);
    }
    
    public String getnbrAvailable() {
        return getTextFieldValue("number available field", nbrAvailableField);
    }
    
    public void clickEditButton() {
        clickButton("edit book button", editBookButton);
    }
    
    public void clickBorrowBookButton() {
        clickButton("borrow book button", borrowBookButton);
    }
    
    public void clickReturnBookButton() {
        clickButton("return book button", returnBookButton);
    }
}
