# -*- coding: UTF-8 -*-

from selenium import webdriver
import time

driver = webdriver.Firefox()

site_url = "http://localhost:8080/librarytest/"

def wait_after_input():
	time.sleep(0.1)  # 100 ms

def wait_page_load():
	time.sleep(1.0)  # 1000 ms
	
def wait_login():
	time.sleep(2.5)  # 2500 ms


username = 'admin'
password = '1234567890'

# Go to Sydcon front page, click "Login" link, confirm navigation to Login page
driver.get(site_url)
wait_after_input()
assert "Library Test Application" in driver.title
driver.find_element_by_id("side-menu-link-sign-in").find_element_by_tag_name("a").click()
wait_page_load()
assert "Sign in" in driver.title

# Log in using the provided credentials, confirm navigation back to start page
driver.find_element_by_id("input-username").send_keys(username)
wait_after_input()
driver.find_element_by_id("input-password").send_keys(password)
wait_after_input()
driver.find_element_by_id("login-button").click()
wait_login()
assert "Library Test Application" in driver.title

# Back on start page, assert correct text on main page and links in sidebar
assert driver.find_element_by_xpath(".//*[@id='main-content']/div/div/div[5]" +
										"/div[contains(text(), 'Logged in as admin.')]")
assert driver.find_element_by_xpath(".//*[@id='side-menu-link-browse-books']/a/span")
assert driver.find_element_by_xpath(".//*[@id='side-menu-link-browse-authors']/a/span")
assert driver.find_element_by_xpath(".//*[@id='side-menu-link-add-book']/a/span")
assert driver.find_element_by_xpath(".//*[@id='side-menu-link-add-author']/a/span")
assert driver.find_element_by_xpath(".//*[@id='side-menu-link-sign-in']/a/span")
assert driver.find_element_by_xpath(".//*[@id='side-menu-link-sign-out']/a/span")
assert driver.find_element_by_xpath(".//*[@id='side-menu-link-my-profile']/a/span")
