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
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> show(String name){
        return jdbcTemplate.query("SELECT * FROM person WHERE name = ?", new Object[]{name}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public Person show(int id) {

        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
        //return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public List<Book> getPersonBooks(int id){
        return jdbcTemplate.query("SELECT B.name as name, author, year  FROM person JOIN Books B on Person.id = B.owner WHERE Person.id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, birth) VALUES (?,?)", person.getName(),person.getBirth());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person set name = ?, birth = ? where id = ?",updatedPerson.getName(),updatedPerson.getBirth(),id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person where id = ?",id);
    }
}
