/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.librarytest.pages;

import com.codeborne.selenide.SelenideElement;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author testautomatisering
 */
public class PageBase {

    private static final Logger LOG = Logger.getLogger(PageBase.class.getName());

    protected void clickButton(String descriptor, SelenideElement element) {
        LOG.log(Level.INFO, "Clicking {0}", descriptor);
        element.click();
    }

    protected void setTextFieldValue(String descriptor, String value, SelenideElement element) {
        LOG.log(Level.INFO, "Setting {0} to {1}", new Object[]{descriptor, value});
        element.clear();
        element.sendKeys(value);
    }
}
