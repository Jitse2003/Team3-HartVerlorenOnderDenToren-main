package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class EventController {

    final private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/admin/event")
    public String event(Model model) {
        List<Event> events = (List<Event>) eventRepository.findAll();
        List<Event> eventsNotActive = eventRepository.findByNotActive();// events where selected = false
        logger.info("amount of events: " + events.size());
        for (Event e : events) {
            logger.info("name event: " + e.getName());
        }
        Optional<Event> event = eventRepository.findByActive();// event where selected = true
        event.ifPresent(value -> model.addAttribute("activeEvent", value.getName()));
        model.addAttribute("events", eventsNotActive);

        return "admin/event";
    }

    @PostMapping("/admin/eventSave")
    public String saveEvent(String name, LocalDate date) {
        Optional<Event> eventActive = eventRepository.findByActive();
        eventActive.ifPresent(event -> event.setSelected(false));
        Event event = new Event(name, date, true);

        eventRepository.save(event);
        return "redirect:event";
    }

    @PostMapping("/admin/eventSelector")
    public String selectEvent(Integer eventId) {

        Optional<Event> event = eventRepository.findById(eventId);
        List<Event> events = (List<Event>) eventRepository.findAll();

        if (event.isPresent()) {
            for (Event e : events) {
                if (Objects.equals(e.getId(), eventId)) {
                    e.setSelected(true);
                    eventRepository.save(event.get());
                } else {
                    e.setSelected(false);
                    eventRepository.save(event.get());
                }
                logger.info("Event " + event.get().getName() + " has been selected");
            }

        } else {
            logger.error("Event not found with ID: " + eventId);
        }

        return "redirect:event";
    }

}
