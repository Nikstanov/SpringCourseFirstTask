package nikstanov.project.DAO;

import nikstanov.project.models.Book;
import nikstanov.project.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    //private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;
    private final PersonDAO personDAO;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory, PersonDAO personDAO) {
        this.sessionFactory = sessionFactory;
        //this.jdbcTemplate = jdbcTemplate;
        this.personDAO = personDAO;
    }

    @Transactional
    public List<Book> index() {
        Session session = sessionFactory.getCurrentSession();

        List<Book> books = session.createQuery("SELECT b FROM Book b", Book.class).getResultList();

        return books;
        //return jdbcTemplate.query("SELECT * FROM books", new BeanPropertyRowMapper<>(Book.class));
    }

    @Transactional
    public void setOwner(int id, int owner_id){
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        Person person = personDAO.show(owner_id);
        book.setOwner(person);
        //jdbcTemplate.update("UPDATE books set owner = ? where id = ?", owner_id, id);
    }

    @Transactional
    public void setOwner(int id){
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        book.setOwner(null);
        //jdbcTemplate.update("UPDATE books set owner = null where id = ?", id);
    }

    @Transactional
    public Book show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
        //return jdbcTemplate.query("SELECT * FROM books WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
        //        .stream().findAny().orElse(null);
        //return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    @Transactional
    public Optional<Book> show(String author, String name){

        Session session = sessionFactory.getCurrentSession();

        Query<Book> query = session.createQuery("SELECT b FROM Book b WHERE name = ?1 AND author = ?2");
        query.setParameter(1,name);
        query.setParameter(2, author);
        return query.getResultList().stream().findFirst();

//        return jdbcTemplate.query("SELECT * FROM books WHERE name = ? AND author = ?", new Object[]{name, author}, new BeanPropertyRowMapper<>(Book.class))
//                .stream().findAny();
    }

    @Transactional
    public Optional<Person> showOwner(int id){
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Book.class, id).getOwner());
//        return jdbcTemplate.query("SELECT P.id as id, P.name as name, birth FROM books join Person P on P.id = Books.owner WHERE Books.id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
//                .stream().findAny();
    }

    @Transactional
    public void save(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.save(book);

//        jdbcTemplate.update("INSERT INTO books(name, author, year) VALUES (?,?,?)", book.getName(),book.getAuthor(),book.getYear());
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        book.setName(updatedBook.getName());
        book.setAuthor(updatedBook.getAuthor());
        book.setYear(updatedBook.getYear());
//        jdbcTemplate.update("UPDATE books set name = ?, author = ?, year = ? where id = ?",updatedBook.getName(),updatedBook.getAuthor(),updatedBook.getYear(),id);
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        session.remove(book);
//        jdbcTemplate.update("DELETE FROM books where id = ?",id);
    }

}
