package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.PerfectDay;
import org.springframework.data.repository.CrudRepository;

public interface PerfectDayRepository extends CrudRepository<PerfectDay, Integer> {
    void deleteByPerfectDay(String perfectDay);
}
