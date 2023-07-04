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
public class PersonDAO {

    //private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        //this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();

        List<Person> people = session.createQuery("SELECT p FROM Person p", Person.class).getResultList();

        return people;
    }

    @Transactional
    public Optional<Person> show(String name){
        Session session = sessionFactory.getCurrentSession();

        Query<Person> query = session.createQuery("SELECT p FROM Person p WHERE name = ?1");
        query.setParameter(1,name);
        return query.getResultList().stream().findFirst();
//        return jdbcTemplate.query("SELECT * FROM person WHERE name = ?", new Object[]{name}, new BeanPropertyRowMapper<>(Person.class))
//                .stream().findAny();
    }

    @Transactional
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);

//        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
//                .stream().findAny().orElse(null);
        //return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    @Transactional
    public List<Book> getPersonBooks(int id){
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        List<Book> books = person.getBooks();
        return books;
        //return jdbcTemplate.query("SELECT B.name as name, author, year  FROM person JOIN Books B on Person.id = B.owner WHERE Person.id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
        //jdbcTemplate.update("INSERT INTO person(name, birth) VALUES (?,?)", person.getName(),person.getBirth());
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        person.setName(updatedPerson.getName());
        person.setBirth(updatedPerson.getBirth());
        //jdbcTemplate.update("UPDATE person set name = ?, birth = ? where id = ?",updatedPerson.getName(),updatedPerson.getBirth(),id);
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        session.remove(person);
        //jdbcTemplate.update("DELETE FROM person where id = ?",id);
    }
}
