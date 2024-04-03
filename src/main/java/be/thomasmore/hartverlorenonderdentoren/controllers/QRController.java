package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.model.Person;
import be.thomasmore.hartverlorenonderdentoren.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class QRController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/qrCodeCheck/{id}")
    public String qrCodeCheck(@PathVariable Integer id, Model model) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            model.addAttribute("booleanQr", person.get().isQrScanned());
            if (!person.get().isQrScanned()) {
                person.get().setQrScanned(true);
                personRepository.save(person.get());
            }
            return "qrCodeCheck";
        }

        return "error";
    }
}