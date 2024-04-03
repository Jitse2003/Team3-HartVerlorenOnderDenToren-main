package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    @Query("SELECT p FROM Person p WHERE " +
            "p.id <> :id AND p.event = (SELECT e FROM Event e WHERE e.selected) AND p.matched = false")
    List<Person> findAllExceptPerson(@Param("id") int id);

    List<Person> findAllByEventAndMatched(Event byActive, boolean matched);

    @Query(nativeQuery = true, value ="SELECT event_id FROM Person p GROUP BY event_id ORDER BY COUNT(id) ASC LIMIT 1")
    Integer findEventWithLeastPersons();

    @Query(nativeQuery = true, value ="SELECT event_id FROM Person p GROUP BY event_id ORDER BY COUNT(id) DESC LIMIT 1")
    Integer findEventWithMostPersons();

    @Query("SELECT COUNT(p) FROM Person p WHERE p.event.id = :id")
    Integer countEvent(@Param("id") int id);


    @Query("SELECT p from Person p where"+
            "(p.event = (SELECT e FROM Event e WHERE e.selected))and "+
            "(p.matched = false ) and"+
            "(:name is null or upper(p.name)  LIKE upper(concat('%', :name, '%')))and " +
            "(:gender is null or upper(p.gender)  like upper(concat('%', :gender, '%')))and" +
            "(:minAge is null or p.birthdate >= :minAge)and " +
            "(:maxAge is null or p.birthdate <= :maxAge)")
    List<Person> findByFilter(@Param("name") String name,
                              @Param("gender") String gender,
                              @Param("minAge") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate minAge,
                              @Param("maxAge") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate maxAge);


}
