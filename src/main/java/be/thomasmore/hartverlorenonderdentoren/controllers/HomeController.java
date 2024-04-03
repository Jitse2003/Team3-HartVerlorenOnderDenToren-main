package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Feedback;
import be.thomasmore.hartverlorenonderdentoren.model.Match;
import be.thomasmore.hartverlorenonderdentoren.model.Person;
import be.thomasmore.hartverlorenonderdentoren.repositories.EventRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.FeedbackRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.MatchRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Controller
public class HomeController {
    final private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MatchRepository matchRepository;

    @GetMapping({"/", "/index"})
    public String home(Principal principal) {
        final String loginName = principal == null ? "NOBODY" : principal.getName();
        logger.info("homepage - logged in as " + loginName);
        return "index";
    }

    @GetMapping({"/matchmaker/analytics"})
    public String Analytics(Model model, @RequestParam(required = false) Integer score, @RequestParam(required = false) Event event) {

        Optional<Event> optionalActiveEvent = eventRepository.findByActive();

        if (optionalActiveEvent.isPresent()) {
            Event activeEvent = optionalActiveEvent.get();
            List<Event> nrOfEvents = (List<Event>) eventRepository.findAll();

            getAnalytics(model, activeEvent);

            //Adding feedback details

            List<Feedback> listFeedback = (List<Feedback>) feedbackRepository.findAll();
            List<Feedback> listFeedbackScoreFiltered = feedbackRepository.findByFilter(score, event);
            Collections.reverse(listFeedback);

            model.addAttribute("feedback", listFeedback);
            model.addAttribute("showFilters", false);
            model.addAttribute("feedbackScoreFiltered", listFeedbackScoreFiltered);

            model.addAttribute("nrOfEvents", nrOfEvents.size());
            model.addAttribute("activeEvent", activeEvent.getName());
        }

        return "matchmaker/analytics";
    }

    @GetMapping({"/matchmaker/analytics/filter"})
    public String AnalyticsFilter(Model model, @RequestParam(required = false) Integer score, @RequestParam(required = false) Event event, @RequestParam(required = false) Integer maxFeedbackAmount) {
        List<Event> nrOfEvents = (List<Event>) eventRepository.findAll();
        Optional<Event> activeEvent = eventRepository.findByActive();
        List<Event> events = (List<Event>) eventRepository.findAll();
        model.addAttribute("events", events);

        if (activeEvent.isPresent()) {
            getAnalytics(model, activeEvent.get());
            model.addAttribute("activeEvent", activeEvent.get().getName());
        }

        //Adding feedback details

        if (maxFeedbackAmount == null) {
            maxFeedbackAmount = 5;
        }

        List<Feedback> listFeedback = (List<Feedback>) feedbackRepository.findAll();
        List<Feedback> listFeedbackScoreFiltered = feedbackRepository.findByFilter(score, event);

        logger.info("maxFeedbackAmount = " + maxFeedbackAmount);

        Collections.reverse(listFeedbackScoreFiltered);
        Collections.reverse(listFeedback);

        model.addAttribute("feedback", listFeedback);
        model.addAttribute("showFilters", true);
        model.addAttribute("feedbackScoreFiltered", listFeedbackScoreFiltered);
        model.addAttribute("maxFeedbackAmount", maxFeedbackAmount);
        model.addAttribute("score", score);

        if (event != null) {
            model.addAttribute("selectedEvent", event);
        } else activeEvent.ifPresent(value -> model.addAttribute("selectedEvent", value));

        model.addAttribute("nrOfEvents", nrOfEvents.size());

        return "matchmaker/analytics";
    }

    public void getAnalytics(Model model, Event activeEvent) {
        //Adding person details

        List<Person> listPersonWithMatches = personRepository.findAllByEventAndMatched(activeEvent, true);
        List<Person> listPersonWithoutMatches = personRepository.findAllByEventAndMatched(activeEvent, false);

        model.addAttribute("listPersonWithMatches", listPersonWithMatches.size());
        model.addAttribute("listPersonWithoutMatches", listPersonWithoutMatches.size());

        //Adding match details

        List<Match> listMatchesPastHour = matchRepository.findAllByTimeAddedAfterAndEvent_Id(LocalTime.now().minusHours(1), activeEvent.getId());
        List<Match> listDeniedMatches = matchRepository.findAllByConfirmationCountAndEvent(-1.0, activeEvent);
        List<Match> listUnconfirmedMatches = matchRepository.findAllByConfirmationCountAndEvent(0.5, activeEvent);
        listUnconfirmedMatches.addAll(matchRepository.findAllByConfirmationCountAndEvent(0.0, activeEvent));
        List<Match> listAcceptedMatches = matchRepository.findAllByConfirmationCountAndEvent(1.0, activeEvent);

        model.addAttribute("listMatchesPastHour", listMatchesPastHour.size());
        model.addAttribute("listDeniedMatches", listDeniedMatches.size());
        model.addAttribute("listUnconfirmedMatches", listUnconfirmedMatches.size());
        model.addAttribute("listAcceptedMatches", listAcceptedMatches.size());

        //Adding person in event details

        Optional<Event> eventWithMostPersons = eventRepository.findById(personRepository.findEventWithMostPersons());
        Optional<Event> eventWithLeastPersons = eventRepository.findById(personRepository.findEventWithLeastPersons());

        if (eventWithMostPersons.isPresent() && eventWithLeastPersons.isPresent()) {
            model.addAttribute("eventWithMostPersons", eventWithMostPersons.get().getName());
            model.addAttribute("eventWithLeastPersons", eventWithLeastPersons.get().getName());
            model.addAttribute("minPersons", personRepository.countEvent(eventWithLeastPersons.get().getId()));
            model.addAttribute("maxPersons", personRepository.countEvent(eventWithMostPersons.get().getId()));
        }

        //Adding person in match details

        if (matchRepository.countAllByEvent(activeEvent) != 0) {
            Optional<Event> eventWithMostMatches = eventRepository.findById(matchRepository.findEventWithMostMatches());
            Optional<Event> eventWithLeastMatches = eventRepository.findById(matchRepository.findEventWithLeastMatches());

            if (eventWithMostMatches.isPresent() && eventWithLeastMatches.isPresent()) {
                model.addAttribute("eventWithMostMatches", eventWithMostMatches.get().getName());
                model.addAttribute("eventWithLeastMatches", eventWithLeastMatches.get().getName());
                model.addAttribute("minMatches", personRepository.countEvent(eventWithLeastMatches.get().getId()));
                model.addAttribute("maxMatches", personRepository.countEvent(eventWithMostMatches.get().getId()));
            }
        } else {
            model.addAttribute("minMatches", 0);
            model.addAttribute("maxMatches", 0);
        }
    }
}
