package se.nackademin.librarytest.helpers;

import static com.codeborne.selenide.Selenide.page;
import java.util.UUID;
import se.nackademin.librarytest.model.Author;
import se.nackademin.librarytest.pages.AddAuthorPage;
import se.nackademin.librarytest.pages.MenuPage;
import se.nackademin.librarytest.pages.BrowseAuthorsPage;
import se.nackademin.librarytest.pages.AuthorPage;

public class AuthorHelper {
    
    public static Author createRandomAuthor() {
        String firstName = UUID.randomUUID().toString().substring(0, 12);
        String lastName = UUID.randomUUID().toString().substring(0, 12);
        String country = UUID.randomUUID().toString().substring(0, 12);
        String biography = UUID.randomUUID().toString();
        String name = firstName + " " + lastName;
        
        Author author = new Author();
        author.setName(name);
        author.setCountry(country);
        author.setBiography(biography);
        return author;
    }
    
    public static void addNewAuthor(Author author) {
        MenuPage menuPage = page(MenuPage.class);
        menuPage.navigateToAddAuthor();  
        
        String[] names = author.getName().split(" ");
        String lastName = names[names.length - 1];
        String firstName = names[0];
        for(int i = 1; i < names.length - 1; i++) {
            firstName += names[1] + " ";
        }
        
        AddAuthorPage addAuthorPage = page(AddAuthorPage.class);
        addAuthorPage.setFirstname(firstName);
        addAuthorPage.setLastname(lastName);
        addAuthorPage.setCountry(author.getCountry());
        addAuthorPage.setBiography(author.getBiography());
        addAuthorPage.clickAddAuthorButton();
    }
    
    public static Author fetchAuthor(String queryName, String queryCountry) {
        MenuPage menuPage = page(MenuPage.class);
        menuPage.navigateToBrowseAuthors();
        
        BrowseAuthorsPage browseAuthorsPage = page(BrowseAuthorsPage.class);
        browseAuthorsPage.setNameField(queryName);
        browseAuthorsPage.setCountryField(queryCountry);
        browseAuthorsPage.clickSearchAuthorsButton();
        browseAuthorsPage.clickFirstResultName();
        
        AuthorPage authorPage = page(AuthorPage.class);
        Author author = new Author();
        author.setName(authorPage.getName());
        author.setCountry(authorPage.getCountry());
        author.setBiography(authorPage.getBiography());
        return author;
    }
    
}
