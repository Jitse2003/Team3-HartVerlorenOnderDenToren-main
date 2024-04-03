package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.CrystalBall;
import org.springframework.data.repository.CrudRepository;

public interface CrystalBallRepo extends CrudRepository<CrystalBall, Integer> {
    void deleteByCrystalBall(String crystalBall);
}
