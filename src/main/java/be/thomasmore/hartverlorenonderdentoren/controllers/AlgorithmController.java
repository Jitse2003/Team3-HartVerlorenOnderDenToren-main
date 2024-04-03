package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.algorithm.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class AlgorithmController {

    private final Logger logger = LoggerFactory.getLogger(MatchesController.class);

    @Autowired
    private Algorithm algorithm;

    @GetMapping("/matchmaker/algorithm/{id}")
    public String algorithm(@PathVariable String id) {

        logger.info("id: " + id);
        algorithm.algorithm(Integer.parseInt(id));

        return "redirect:/matchmaker/matchesOverview/" + id;
    }

}
