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
public class SignInPage extends MenuPage {
    
    @FindBy(css = "#input-username")
    SelenideElement userNameField;
    @FindBy(css = "#input-password")
    SelenideElement passwordField;
    @FindBy(css = "#login-button")
    SelenideElement logInButton;

    public void setUsername(String username) {
        setTextFieldValue("user name field", username, userNameField);
    }

    public void setPassword(String password) {
        setTextFieldValue("password field", password, passwordField);
    }

    public void clickLogIn() {
        clickButton("log in button", logInButton);
    }
}
