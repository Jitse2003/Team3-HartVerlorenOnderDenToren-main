package be.thomasmore.hartverlorenonderdentoren.repositories;

import be.thomasmore.hartverlorenonderdentoren.model.InterestsPreference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestsPreferenceRepository extends CrudRepository<InterestsPreference, Integer> {
    void deleteByInterestsPreference(String interestsPreference);

    @Query("SELECT i FROM InterestsPreference i WHERE"+
            "(i.id in :interestPreferenceIds)")
    List<InterestsPreference> findAllByIds(@Param("interestPreferenceIds") List<Integer> interestPreferenceIds);

}