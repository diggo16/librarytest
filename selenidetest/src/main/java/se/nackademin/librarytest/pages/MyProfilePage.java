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
public class MyProfilePage extends MenuPage {
    @FindBy(css = "#gwt-uid-5")
    SelenideElement userNameField;

    public String getUserName() {
        return userNameField.getText();
    }
}
