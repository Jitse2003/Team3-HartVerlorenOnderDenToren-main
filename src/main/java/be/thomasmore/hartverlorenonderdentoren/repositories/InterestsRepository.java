package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.Interests;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestsRepository extends CrudRepository<Interests, Integer> {
    void deleteByInterests(String interest);

    @Query("SELECT i FROM Interests i WHERE"+
            "(i.id in :interestIds)")
    List<Interests> findAllByIds(@Param("interestIds") List<Integer> interestIds);

    List<Interests> findAll();
}
