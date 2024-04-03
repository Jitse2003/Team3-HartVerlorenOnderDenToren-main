package be.thomasmore.hartverlorenonderdentoren.algorithm;

import be.thomasmore.hartverlorenonderdentoren.controllers.MatchesController;
import be.thomasmore.hartverlorenonderdentoren.model.Person;
import be.thomasmore.hartverlorenonderdentoren.model.RecommendedMatch;
import be.thomasmore.hartverlorenonderdentoren.repositories.PersonRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.RecommendedMatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class Algorithm {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RecommendedMatchRepository recommendedMatchRepository;

    private final Logger logger = LoggerFactory.getLogger(MatchesController.class);

    public void algorithm(int id) {
        List<List<Integer>> matchCalculated = new ArrayList<>();

        boolean algorithmExecuted = recommendedMatchRepository.algorithmExecutedForPerson(id) > 0;

        if (!algorithmExecuted) {
            Person personSearchMatch;
            if (personRepository.findById(id).isPresent()) {
                personSearchMatch = personRepository.findById(id).get();
            } else {
                return;
            }

            List<Person> otherPeopleInDb = personRepository.findAllExceptPerson(id);

            for (Person p : otherPeopleInDb) {
                int percentage = 0;
                boolean preferredGender;
                List<Integer> person = new ArrayList<>();
                List<String> convertedInterests = Stream.of(personSearchMatch.getInterestsPreference().split(",", -1)).toList();
                List<String> convertedMutualInterests = Stream.of(p.getInterests().split(",", -1)).toList();

                int searchAmount = 4 + convertedInterests.size();

                int calculatedPartialPercentage = 100 / searchAmount;

                preferredGender = p.getGender().equals(personSearchMatch.getGenderPreference());

                if (preferredGender) {
                    for (String s : convertedInterests) {
                        if (convertedMutualInterests.contains(s)) {
                            percentage += calculatedPartialPercentage;
                        }
                    }

                    boolean ageCheck1 = (ChronoUnit.YEARS.between(p.getBirthdate(),
                            LocalDate.now()) >= personSearchMatch.getMinAgePreference()) &&
                            ChronoUnit.YEARS.between(p.getBirthdate(),
                                    LocalDate.now()) <= personSearchMatch.getMaxAgePreference();
                    boolean ageCheck2 = (ChronoUnit.YEARS.between(personSearchMatch.getBirthdate(),
                            LocalDate.now()) >= p.getMinAgePreference()) &&
                            ChronoUnit.YEARS.between(personSearchMatch.getBirthdate(),
                                    LocalDate.now()) <= p.getMaxAgePreference();

                    if (ageCheck1 && ageCheck2) {
                        percentage += calculatedPartialPercentage;
                    }

                    if (personSearchMatch.getCrystalBall().equals(p.getCrystalBall())){
                        percentage += calculatedPartialPercentage;
                    }

                    if (personSearchMatch.getPerfectDay().equals(p.getPerfectDay())){
                        percentage += calculatedPartialPercentage;
                    }

                    if (personSearchMatch.getHouseOnFire().equals(p.getHouseOnFire())){
                        percentage += calculatedPartialPercentage;
                    }

                }
                person.add(p.getId());
                person.add(percentage);
                matchCalculated.add(person);
            }

            List<List<Integer>> SortedMatches = selectionSort(matchCalculated);

            for (int i = SortedMatches.size() - 1; i > SortedMatches.size() - 4; i--) {
                List<Integer> match = SortedMatches.get(i);

                Optional<Person> topMatch = personRepository.findById(match.get(0));
                if (topMatch.isPresent()) {
                    RecommendedMatch recommendedMatch = new RecommendedMatch(personSearchMatch, topMatch.get(), match.get(1));
                    logger.info("match: " + recommendedMatch.getPerson1().getName() + " with " + recommendedMatch.getPerson2().getName() + " percentage: " + recommendedMatch.getPercentage());
                    recommendedMatchRepository.save(recommendedMatch);
                }
            }
        }
    }

    public List<List<Integer>> selectionSort(List<List<Integer>> matchCalculated) {
        int n = matchCalculated.size();

        // Loop to increase the boundary of the sorted array
        for (int i = 0; i < n - 1; i++) {

            // Finding the smallest element in the unsorted array
            int min_element = i;
            for (int j = i + 1; j < n; j++)
                if (matchCalculated.get(j).get(1) < matchCalculated.get(min_element).get(1))
                    min_element = j;

            /* Swap the smallest element from the unsorted array with the last element of the sorted array */
            List<Integer> temp = matchCalculated.get(min_element);
            matchCalculated.set(min_element, matchCalculated.get(i));
            matchCalculated.set(i, temp);
        }
        return matchCalculated;
    }
}
