package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.HouseOnFire;
import org.springframework.data.repository.CrudRepository;

public interface HouseOnFireRepository extends CrudRepository<HouseOnFire, Integer> {
    void deleteByHouseOnFire(String houseOnFire);
}
