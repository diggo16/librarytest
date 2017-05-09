package se.nackademin.librarytest;

import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

import se.nackademin.librarytest.helpers.AuthorHelper;
import se.nackademin.librarytest.helpers.BookHelper;
import se.nackademin.librarytest.helpers.UserHelper;
import se.nackademin.librarytest.model.Author;
import se.nackademin.librarytest.model.Book;
import se.nackademin.librarytest.model.User;

public class SelenideTest extends TestBase {

    public SelenideTest() {
        System.setProperty("webdriver.chrome.driver", "/home/daniel/seleniumdrivers/chromedriver");
        System.setProperty("selenide.browser", "Chrome");
    }
    
    @Test
    public void testCreateNewAuthor() {
        UserHelper.logInAsUser("admin", "1234567890");
        
        Author author = AuthorHelper.createRandomAuthor();
        AuthorHelper.addNewAuthor(author);
        
        Author fetchedAuthor = AuthorHelper.fetchAuthor(author.getName(), author.getCountry());
        assertEquals("name should be the same", author.getName(), fetchedAuthor.getName());
        assertEquals("country should be the same", author.getCountry(), fetchedAuthor.getCountry());
        assertEquals("biography should be the same", author.getBiography(), fetchedAuthor.getBiography());
        
        UserHelper.signOut();
        assertFalse("Should be logged out", UserHelper.isLoggedIn());
    }
    
    @Test
    public void testSetEmailAddress() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        UserHelper.createNewUser(username, password);
        
        UserHelper.logInAsUser(username, password);
        
        User oldUser = UserHelper.fetchUser();
        String email = UUID.randomUUID().toString().substring(0, 6) + "@hotmail.com";
        oldUser.setEmail(email);
        UserHelper.setUser(oldUser);

        User newUser = UserHelper.fetchUser();
        
        assertEquals("emails should be the same", email, newUser.getEmail());
        
        UserHelper.signOut();   
        assertFalse("Should be logged out", UserHelper.isLoggedIn());
    }
    
    @Test
    public void testSetDatePublished() {
        UserHelper.logInAsUser("admin", "1234567890");
        String title = "Good Omens";
        
        Book oldBook = BookHelper.fetchBook(title);
       
        String datePublished = BookHelper.getRandomDatePublished();
        oldBook.setDatePublished(datePublished);
        BookHelper.setBook(oldBook);
        
        Book newBook = BookHelper.fetchBook(title);
        assertEquals("Date published should be updated", datePublished, newBook.getDatePublished());
        
        UserHelper.signOut();
        assertFalse("Should be logged out", UserHelper.isLoggedIn());
    }
    
    @Test
    public void testBorrowBook() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        UserHelper.createNewUser(username, password);
        
        UserHelper.logInAsUser(username, password);
        String title = "American Gods";
        int nbrAvailable = BookHelper.fetchBook(title).getNbrAvailable();
        nbrAvailable--;     // One less book available when you borrow one.
   
        BookHelper.borrowBook(title);
        int newNbrAvailable = BookHelper.fetchBook(title).getNbrAvailable();
        assertEquals("Numbers of books available should be one less than before", nbrAvailable, newNbrAvailable);     
        
        assertTrue("Book should be borrowed", BookHelper.isBookBorrowed(title));
        
        BookHelper.returnBook(title);
        
        assertFalse("Book shouldn't be borrowed", BookHelper.isBookBorrowed(title));
        
        UserHelper.signOut();
        assertFalse("Should be logged out", UserHelper.isLoggedIn());
    }
}
