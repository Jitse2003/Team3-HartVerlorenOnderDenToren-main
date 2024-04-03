package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.Person;
import be.thomasmore.hartverlorenonderdentoren.model.RecommendedMatch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendedMatchRepository extends CrudRepository<RecommendedMatch, Integer> {

    @Query("SELECT r FROM RecommendedMatch r WHERE r.person1 = :person")
    List<RecommendedMatch> findAllByPerson1(@Param("person") Person person);

    @Query("SELECT COUNT(r.id) FROM RecommendedMatch r WHERE r.person1.id = :id")
    int algorithmExecutedForPerson(@Param("id") int id);
}
