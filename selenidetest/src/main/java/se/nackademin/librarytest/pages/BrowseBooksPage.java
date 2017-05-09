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
public class BrowseBooksPage extends MenuPage {
    @FindBy(css = "#gwt-uid-3")
    private SelenideElement titleField;
    @FindBy(css = "#gwt-uid-7")
    private SelenideElement isbnField;
    @FindBy(css = "#search-books-button")
    private SelenideElement searchBooksButton;
    @FindBy(css = "td.v-grid-cell:nth-child(1) > a:nth-child(1)")
    private SelenideElement firstResultTitle;

    public void clickFirstResultTitle() {
        clickButton("first result title", firstResultTitle);
    }

    public void setTitleField(String title) {
        setTextFieldValue("title field", title, titleField);
    }
    
    public void setIsbnField(String isbn) {
        setTextFieldValue("isbn field", isbn, isbnField);
    }

    public void clickSearchBooksButton() {
        clickButton("search books button", searchBooksButton);
    }
}
