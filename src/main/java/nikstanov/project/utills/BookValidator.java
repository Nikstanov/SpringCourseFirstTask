package nikstanov.project.utills;

import nikstanov.project.DAO.BookDAO;
import nikstanov.project.DAO.PersonDAO;
import nikstanov.project.models.Book;
import nikstanov.project.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BookDAO bookDAO;

    @Autowired
    public BookValidator(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        if(bookDAO.show(book.getAuthor(), book.getName()).isPresent()){
            errors.rejectValue("name", "", "This book already exists");
        }
    }
}
