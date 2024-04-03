package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Feedback;
import be.thomasmore.hartverlorenonderdentoren.model.Person;
import be.thomasmore.hartverlorenonderdentoren.repositories.FeedbackRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class FeedbackController {
    @Autowired
    private PersonRepository personrepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("feedback/{personId}")
    public String feedback(Model model, @PathVariable Integer personId) {
        Optional<Person> person = personrepository.findById(personId);
        person.ifPresent(value -> model.addAttribute("person", value));

        return "feedback";
    }

    @PostMapping("feedback/{personId}")
    public String saveFeedback(@PathVariable Integer personId,
                               @RequestParam Integer score,
                               @RequestParam String feedbackText) {

        Optional<Person> person = personrepository.findById(personId);
        if (person.isPresent()) {
            Event event = person.get().getEvent();
            Feedback feedback = new Feedback(score, feedbackText, event);
            feedbackRepository.save(feedback);
        }

        return "redirect:/index";
    }
}
