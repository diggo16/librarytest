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
public class EditUserPage extends MenuPage {
    
    @FindBy(css = "#gwt-uid-3")
    SelenideElement userNameField;
    @FindBy(css = "#gwt-uid-5")
    SelenideElement passwordField;
    @FindBy(css = "#gwt-uid-7")
    SelenideElement firstNameField;
    @FindBy(css = "#gwt-uid-9")
    SelenideElement lastNameField;
    @FindBy(css = "#gwt-uid-11")
    SelenideElement phoneField;
    @FindBy(css = "#gwt-uid-13")
    SelenideElement emailField;
    @FindBy(css = "#save-user-button")
    SelenideElement saveUserButton;
    
    public void setUserNameField(String name) {
        setTextFieldValue("User name field", name, userNameField);
    }
    
    public void setPasswordField(String password) {
        setTextFieldValue("password field", password, passwordField);
    }
    
    public void setFirstNameField(String firstName) {
        setTextFieldValue("First name field", firstName, firstNameField);
    }
    
    public void setLastNameField(String lastName) {
        setTextFieldValue("Last name field", lastName, lastNameField);
    }
    
    public void setPhoneField(String phone) {
        setTextFieldValue("Phone field", phone, phoneField);
    }
    
    public void setEmailField(String email) {
        setTextFieldValue("Email field", email, emailField);
    }
    
    public void clickSaveUserButton() {
        clickButton("Save user button", saveUserButton);
    }
}
