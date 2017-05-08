# -*- coding: utf-8 -*-
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import NoAlertPresentException
import unittest, time, re

class ShouldLogInAsAdmin(unittest.TestCase):
    def setUp(self):
        self.driver = webdriver.Firefox()
        self.driver.implicitly_wait(30)
        self.base_url = "http://localhost:8080/"
        self.verificationErrors = []
        self.accept_next_alert = True
    
    def test_should_log_in_as_admin(self):
        driver = self.driver
        driver.get(self.base_url + "librarytest/")
        for i in range(60):
            try:
                if "Welcome to the Library Test Application!\n\n\nPlease choose an action in the left-hand side menu." == driver.find_element_by_xpath("//div[@id='main-content']/div/div").text: break
            except: pass
            time.sleep(1)
        else: self.fail("time out")
        try: self.assertEqual("Sign in", driver.find_element_by_css_selector("#side-menu-link-sign-in > a > span").text)
        except AssertionError as e: self.verificationErrors.append(str(e))
        driver.find_element_by_css_selector("#side-menu-link-sign-in > a > span").click()
        for i in range(60):
            try:
                if "Sign in" == driver.title: break
            except: pass
            time.sleep(1)
        else: self.fail("time out")
        driver.find_element_by_id("input-username").clear()
        driver.find_element_by_id("input-username").send_keys("admin")
        driver.find_element_by_id("input-password").clear()
        driver.find_element_by_id("input-password").send_keys("1234567890")
        driver.find_element_by_id("login-button").click()
        for i in range(60):
            try:
                if "Logged in as admin." == driver.find_element_by_xpath("//div[@id='main-content']/div/div/div[5]/div").text: break
            except: pass
            time.sleep(1)
        else: self.fail("time out")
    
    def is_element_present(self, how, what):
        try: self.driver.find_element(by=how, value=what)
        except NoSuchElementException as e: return False
        return True
    
    def is_alert_present(self):
        try: self.driver.switch_to_alert()
        except NoAlertPresentException as e: return False
        return True
    
    def close_alert_and_get_its_text(self):
        try:
            alert = self.driver.switch_to_alert()
            alert_text = alert.text
            if self.accept_next_alert:
                alert.accept()
            else:
                alert.dismiss()
            return alert_text
        finally: self.accept_next_alert = True
    
    def tearDown(self):
        self.driver.quit()
        self.assertEqual([], self.verificationErrors)

if __name__ == "__main__":
    unittest.main()
