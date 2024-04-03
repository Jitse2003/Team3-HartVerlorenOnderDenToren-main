package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Sponsor;
import be.thomasmore.hartverlorenonderdentoren.repositories.EventRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.SponsorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class SponsorController {

    final private Logger logger = LoggerFactory.getLogger(SponsorController.class);

    @Autowired
    private SponsorRepository sponsorRepository;
    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/admin/sponsor")
    public String AddSponsor(Model model){
        List<Sponsor> sponsorList = (List<Sponsor>) sponsorRepository.findAll();
        List<Event> events = (List<Event>) eventRepository.findAll();
        logger.info("amount of sponsors: " + sponsorList.size());
        for (Sponsor s : sponsorList) {
            logger.info("name sponsor: " + s.getName());
        }
        model.addAttribute("sponsors", sponsorList);
        model.addAttribute("events", events);
        return "admin/sponsor";
    }

    @PostMapping("/admin/sponsorSave")
    public String AddSponsorSave(@RequestParam String name,
                                 @RequestParam String address,
                                 @RequestParam String naamVerantwoordelijke,
                                 @RequestParam String telefoonnummer,
                                 @RequestParam Collection<Event> event){
        Sponsor sponsor = new Sponsor(name, address, telefoonnummer, naamVerantwoordelijke, event);
        sponsor.setName(name);
        sponsor.setAddress(address);
        sponsor.setTelefoonnummer(telefoonnummer);
        sponsor.setNaamVerantwoordelijke(naamVerantwoordelijke);
        sponsor.setEvent(event);
        sponsorRepository.save(sponsor);
        logger.info("new sponsor added : "+ sponsor.getName());
        return "redirect:sponsor";
    }

    @PostMapping("/admin/sponsorSelect")
    public String addSponsorToEvent(@RequestParam Integer sponsorId,
                                    @RequestParam Integer eventId) {
        Optional<Sponsor> sponsorOptional = sponsorRepository.findById(sponsorId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (sponsorOptional.isPresent() && eventOptional.isPresent()) {
            Sponsor sponsor = sponsorOptional.get();
            Event event = eventOptional.get();

            // Add the event to the sponsor's events collection
            sponsor.getEvent().add(event);
            sponsorRepository.save(sponsor);

            // Add the sponsor to the event's sponsors collection
            event.getSponsors().add(sponsor);
            eventRepository.save(event);
            logger.info("" + event.getName() + " is getting sponsored by " + sponsor.getName());
        }

        return "redirect:/admin/sponsor";
    }


}
