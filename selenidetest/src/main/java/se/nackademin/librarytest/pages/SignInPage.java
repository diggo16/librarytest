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
        userNameField.clear();
        userNameField.sendKeys(username);

    }

    public void setPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogIn() {
        logInButton.click();
    }
}
