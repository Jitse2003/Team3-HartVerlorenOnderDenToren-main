package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Sponsor;
import be.thomasmore.hartverlorenonderdentoren.model.Stand;
import be.thomasmore.hartverlorenonderdentoren.repositories.EventRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.SponsorRepository;
import be.thomasmore.hartverlorenonderdentoren.repositories.StandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StandController {

    final private Logger logger = LoggerFactory.getLogger(StandController.class);

    @Autowired
    private StandRepository standRepository;
    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/admin/stand")
    private String stand(Model model) {
        List<Stand> standList = (List<Stand>) standRepository.findAll();
        List<Sponsor> sponsorList = (List<Sponsor>) sponsorRepository.findAll();
        List<Event> eventList = (List<Event>) eventRepository.findAll();
        logger.info("amount of stands: " + standList.size());
        for (Stand s : standList) {
            logger.info("name stand: " + s.getName());
        }
        model.addAttribute("standList", standList);
        model.addAttribute("sponsorList", sponsorList);
        model.addAttribute("eventList", eventList);

        return "admin/stand";
    }

    @PostMapping("/admin/standSave")
    private String standSave(@RequestParam String name,
                             @RequestParam String nameResponsiblePerson,
                             @RequestParam String info,
                             @RequestParam Sponsor sponsor,
                             @RequestParam Event event) {
        Stand stand = new Stand(name, nameResponsiblePerson, info, sponsor, event);
        stand.setName(name);
        stand.setNameResponsiblePerson(nameResponsiblePerson);
        stand.setInfo(info);
        stand.setSponsor(sponsor);
        stand.setEvent(event);
        standRepository.save(stand);
        logger.info("stand has been added : " + stand.getName());
        return "redirect:stand";
    }

}
