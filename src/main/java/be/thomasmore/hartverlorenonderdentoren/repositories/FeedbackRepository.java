package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Feedback;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends CrudRepository<Feedback, Integer> {
    @Query("SELECT v FROM Feedback v WHERE"+
            "(:score IS NULL OR v.score = :score) AND " +
            "(:event IS NULL OR v.event = :event)")
    List<Feedback> findByFilter(@Param("score") Integer score,
                                @Param("event") Event event);
}
