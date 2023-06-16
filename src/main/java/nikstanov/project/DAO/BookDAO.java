package nikstanov.project.DAO;

import nikstanov.project.models.Book;
import nikstanov.project.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM books", new BeanPropertyRowMapper<>(Book.class));
    }

    public void setOwner(int id, int owner_id){
        jdbcTemplate.update("UPDATE books set owner = ? where id = ?", owner_id, id);
    }

    public void setOwner(int id){
        jdbcTemplate.update("UPDATE books set owner = null where id = ?", id);
    }

    public Book show(int id) {

        return jdbcTemplate.query("SELECT * FROM books WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
        //return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public Optional<Book> show(String author, String name){
        return jdbcTemplate.query("SELECT * FROM books WHERE name = ? AND author = ?", new Object[]{name, author}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny();
    }

    public Optional<Person> showOwner(int id){
        return jdbcTemplate.query("SELECT P.id as id, P.name as name, birth FROM books join Person P on P.id = Books.owner WHERE Books.id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO books(name, author, year) VALUES (?,?,?)", book.getName(),book.getAuthor(),book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE books set name = ?, author = ?, year = ? where id = ?",updatedBook.getName(),updatedBook.getAuthor(),updatedBook.getYear(),id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM books where id = ?",id);
    }

}
