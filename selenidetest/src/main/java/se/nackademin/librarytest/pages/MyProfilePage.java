package se.nackademin.librarytest.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class MyProfilePage extends MenuPage {
    
    @FindBy(css = "#gwt-uid-5")
    SelenideElement userNameField;
    @FindBy(css = "#gwt-uid-7")
    SelenideElement firstNameField;
    @FindBy(css = "#gwt-uid-9")
    SelenideElement lastNameField;
    @FindBy(css = "#gwt-uid-11")
    SelenideElement phoneField;
    @FindBy(css = "#gwt-uid-13")
    SelenideElement emailField;
    @FindBy(css = "#edit-user")
    SelenideElement editUserButton;
    @FindBy(css = "td.v-grid-cell:nth-child(1) > a:nth-child(1)")
    private SelenideElement firstLoanTitle;

    public String getUserName() {
        return getTextFieldValue("User name field", userNameField);
    }
    
    public String getFirstName() {
        return getTextFieldValue("First name field", firstNameField);
    }
    
    public String getLastName() {
        return getTextFieldValue("Last name field", lastNameField);
    }
    
    public String getPhone() {
        return getTextFieldValue("Phone field", phoneField);
    }
    
    public String getEmail() {
        return getTextFieldValue("Email field", emailField);
    }
    
    public void clickEditUserButton() {
        clickButton("Edit user button", editUserButton);
    }
    
    public boolean clickFirstLoanTitle() {
        if(firstLoanTitle.isDisplayed()) {
            clickButton("first loan title", firstLoanTitle);
            return true;
        }
        return false;    
    }
}
