package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends CrudRepository<Match, Integer> {
    Optional<Match> findMatchByPerson1_Id(Integer personId);

    Optional<Match> findMatchByPerson2_Id(Integer personId);

    List<Match> findAllByConfirmationCountAndEvent(Double confirmationCount, Event event);

    List<Match> findAllByTimeAddedAfterAndEvent_Id(LocalTime localTime, Integer eventId);

    int countAllByEvent(Event event);

    @Query(nativeQuery = true, value ="SELECT event_id FROM Match m GROUP BY event_id ORDER BY COUNT(id) ASC LIMIT 1")
    Integer findEventWithLeastMatches();

    @Query(nativeQuery = true, value ="SELECT event_id FROM Match m GROUP BY event_id ORDER BY COUNT(id) DESC LIMIT 1")
    Integer findEventWithMostMatches();
}
