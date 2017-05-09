/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.librarytest.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author daniel
 */
public class EditBookPage extends MenuPage {
    @FindBy(css = "#gwt-uid-3")
    SelenideElement titleField;
    @FindBy(css = "#gwt-uid-9")
    SelenideElement descriptionField;
    @FindBy(css = "#gwt-uid-13")
    SelenideElement isbnField;
    @FindBy(css = "#gwt-uid-5")
    SelenideElement nbrAvailableField;
    @FindBy(css = "#gwt-uid-7")
    SelenideElement datePublishedField;
    @FindBy(css = "#save-book-button")
    SelenideElement saveBookButton;
    
    public void setTitleField(String title) {
        setTextFieldValue("title field", title, titleField);
    }
    
    public void setDescriptionField(String description) {
        setTextFieldValue("description field", description, descriptionField);
    }
    
    public void setIsbnField(String isbn) {
        setTextFieldValue("isbn field", isbn, isbnField);
    }
    
    public void setNbrAvailableField(String nbrAvailable) {
        setTextFieldValue("number Available field", nbrAvailable, nbrAvailableField);
    }

    public void setDatePublishedField(String datePublished) {
        setTextFieldValue("date published field", datePublished, datePublishedField);
    }

    public void clickSaveBookButton() {
        clickButton("save book button", saveBookButton);
    }
}
