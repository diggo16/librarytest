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
public class ConfirmDialogPage extends PageBase {
    
    @FindBy(css = "#confirmdialog-ok-button")
    SelenideElement okButton;
    @FindBy(css = "#confirmdialog-cancel-button")
    SelenideElement cancelButton;
    
    public void clickOkButton() {
        clickButton("ok button", okButton);
    }
    
    public void clickCancelButton() {
        clickButton("cancel button", cancelButton);
    }
}
