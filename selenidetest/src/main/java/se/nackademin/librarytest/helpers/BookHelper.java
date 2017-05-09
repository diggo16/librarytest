/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nackademin.librarytest.helpers;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import org.openqa.selenium.NoSuchElementException;

import se.nackademin.librarytest.model.Book;
import se.nackademin.librarytest.pages.BookPage;
import se.nackademin.librarytest.pages.BrowseBooksPage;
import se.nackademin.librarytest.pages.ConfirmDialogPage;
import se.nackademin.librarytest.pages.EditBookPage;
import se.nackademin.librarytest.pages.MenuPage;
import se.nackademin.librarytest.pages.MyProfilePage;

/**
 * @author testautomatisering
 */
public class BookHelper {

 public static Book fetchBook(String searchQuery) {
        searchForBookAndClick(searchQuery);

        BookPage bookPage = page(BookPage.class);
        Book book = new Book();
        book.setTitle(bookPage.getTitle());
        book.setAuthor(bookPage.getAuthor());
        book.setDescription(bookPage.getDescription());
        book.setIsbn(bookPage.getIsbn());
        book.setDatePublished(bookPage.getDatePublished());
        book.setNbrAvailable(Integer.parseInt(bookPage.getnbrAvailable()));
        return book;
    }
    
    public static void setBook(Book book) {
        searchForBookAndClick(book.getTitle());
        
        BookPage bookPage = page(BookPage.class);
        bookPage.clickEditButton();
        
        EditBookPage editBookPage = page(EditBookPage.class);
        editBookPage.setTitleField(book.getTitle());
        editBookPage.setIsbnField(book.getIsbn());
        editBookPage.setNbrAvailableField(Integer.toString(book.getNbrAvailable()));
        editBookPage.setDatePublishedField(book.getDatePublished());
        editBookPage.clickSaveBookButton();
    }
    
    public static void borrowBook(String searchQuery) {
        searchForBookAndClick(searchQuery);
        
        BookPage bookPage = page(BookPage.class);
        bookPage.clickBorrowBookButton();
        
        ConfirmDialogPage confirmDialogPage = page(ConfirmDialogPage.class);
        confirmDialogPage.clickOkButton();
    }
    
    public static boolean isBookBorrowed(String searchQuery) {
        MenuPage menuPage = page(MenuPage.class);
        menuPage.navigateToMyProfile();
        
        MyProfilePage myProfilePage = page(MyProfilePage.class);
        myProfilePage.clickFirstLoanTitle();
        
        BookPage bookPage = page(BookPage.class);
        return searchQuery.equals(bookPage.getTitle());
    }
    
    public static void returnBook(String searchQuery) {
        searchForBookAndClick(searchQuery);
        
        BookPage bookPage = page(BookPage.class);
        bookPage.clickReturnBookButton();
        
        ConfirmDialogPage confirmDialogPage = page(ConfirmDialogPage.class);
        confirmDialogPage.clickOkButton();
    }
    
    public static String getRandomDatePublished() {
        Random rand = new Random();
        int randomYear = rand.nextInt(110)+1900;
        int dayOfYear = rand.nextInt(364)+1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, randomYear);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String datePublished = format1.format(calendar.getTime());
        return datePublished;
    }
    
    private static void searchForBookAndClick(String title) {
        MenuPage menuPage = page(MenuPage.class);
        menuPage.navigateToBrowseBooks();
        
        BrowseBooksPage browseBooksPage = page(BrowseBooksPage.class);
        browseBooksPage.setTitleField(title);
        browseBooksPage.clickSearchBooksButton();
        browseBooksPage.clickFirstResultTitle();
    }
}
