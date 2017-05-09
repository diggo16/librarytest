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
public class BrowseAuthorsPage extends MenuPage{
    
    @FindBy(css = "#gwt-uid-3")
    private SelenideElement nameField;
    @FindBy(css = "#gwt-uid-5")
    private SelenideElement countryField;
    @FindBy(css = "#search-authors-button")
    private SelenideElement searchAuthorsButton;
    @FindBy(css = ".v-grid-cell-focused > a:nth-child(1)")
    private SelenideElement firstResultName;
    
    public void setNameField(String name) {
        setTextFieldValue("name field", name, nameField);
    }
    
    public void setCountryField(String country) {
        setTextFieldValue("country field", country, countryField);
    }

    public void clickSearchAuthorsButton() {
        clickButton("search authors button", searchAuthorsButton);
    }
    
    public void clickFirstResultName() {
        clickButton("first result name", firstResultName);
    }
    
}
