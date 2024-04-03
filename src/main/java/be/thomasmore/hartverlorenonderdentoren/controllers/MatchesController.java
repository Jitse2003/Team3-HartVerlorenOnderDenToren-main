package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.QR.QRCodeGenerator;
import be.thomasmore.hartverlorenonderdentoren.model.Event;
import be.thomasmore.hartverlorenonderdentoren.model.Match;
import be.thomasmore.hartverlorenonderdentoren.model.Person;
import be.thomasmore.hartverlorenonderdentoren.model.RecommendedMatch;
import be.thomasmore.hartverlorenonderdentoren.model.Stand;
import be.thomasmore.hartverlorenonderdentoren.repositories.*;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Configuration
@EnableScheduling
@Controller
public class MatchesController {

    private final Logger logger = LoggerFactory.getLogger(MatchesController.class);

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RecommendedMatchRepository recommendedMatchRepository;
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StandRepository standRepository;
    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @GetMapping({"/matchmaker/matchesOverview/{id}"})
    public String matchesOverview(Model model, @PathVariable String id) {

        Optional<Person> personInDb = personRepository.findById(Integer.valueOf(id));

        if (personInDb.isPresent()) {
            List<RecommendedMatch> personMatches = recommendedMatchRepository.findAllByPerson1(personInDb.get());
            RecommendedMatch firstMatch = personMatches.get(0);
            personMatches.remove(0);

            for (RecommendedMatch p : personMatches) {
                logger.info("match: " + p.getPerson2().getName());
            }

            int sizeMatches = personMatches.size();
            logger.info("amount of matches: " + sizeMatches);

            model.addAttribute("person1", personInDb.get());
            model.addAttribute("firstMatch", firstMatch);
            model.addAttribute("personMatches", personMatches);
            model.addAttribute("sizeMatches", sizeMatches);
        }

        return "matchmaker/matchesOverview";
    }

    @GetMapping("/matchmaker/makeMatch/{personId1}/{personId2}")
    public String addMatch(Model model,
                           @PathVariable String personId1,
                           @PathVariable String personId2) {

        Optional<Person> person1 = personRepository.findById(Integer.valueOf(personId1));
        Optional<Person> person2 = personRepository.findById(Integer.valueOf(personId2));

        if (person1.isPresent() && person2.isPresent()) {
            logger.info("match: " + person1.get().getName() + " and " + person2.get().getName());

            model.addAttribute("person1", person1.get());
            model.addAttribute("person2", person2.get());
            model.addAttribute("today", LocalDate.now());
        }

        return "matchmaker/makeMatch";
    }

    @PostMapping("/matchmaker/makeMatch/{personId1}/{personId2}")
    public String sendEmail(@PathVariable String personId1,
                            @PathVariable String personId2) throws MessagingException, IOException, WriterException {
        Optional<Person> person1 = personRepository.findById(Integer.valueOf(personId1));
        Optional<Person> person2 = personRepository.findById(Integer.valueOf(personId2));

        if (person1.isPresent() && person2.isPresent() && eventRepository.findByActive().isPresent()) {
            logger.info("match: " + person1.get().getName() + " and " + person2.get().getName());

            Match match = new Match(person1.get(), person2.get(), false, LocalTime.now(), eventRepository.findByActive().get());
            match.getPerson1().setMatched(true);
            match.getPerson2().setMatched(true);


            matchRepository.save(match);

            String bodyConfirm1 = mailConfirmBody(personId2, person1.get());
            String bodyConfirm2 = mailConfirmBody(personId1, person2.get());

            email(person1.get(), "Uw match is gevonden!", bodyConfirm1, false);
            email(person2.get(), "Uw match is gevonden!", bodyConfirm2, false);
        }

        return "redirect:/index";
    }

    private String mailConfirmBody(@PathVariable String personId, Person person) {
        return "<html> \n" +
                "<head> \n" +
                "<style>\n" +
                "h2," +
                "h3," +
                "h4 {\n" +
                "  color:rgb(114, 28, 37)\n" +
                "}" +
                "button  {\n" +
                "    background-color: #ff4d4d;\n" +
                "    color: #fff;\n" +
                "    border: none;\n" +
                "    border-radius: 8px;\n" +
                "    font-size: 15px;\n" +
                "    font-weight: bold;\n" +
                "    text-transform: uppercase;\n" +
                "    padding: 12px 20px;\n" +
                "    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.3);\n" +
                "    transition: all 0.2s ease-in-out;\n" +
                "} \n" +
                "button:hover {\n" +
                "    background-color: #e60000;\n" +
                "    cursor: pointer;\n" +
                "    transform: scale(1.05);\n" +
                "}\n" +
                "  div {\n" +
                "    text-align: center;\n" +
                "    background-color: rgb(248,186,189);\n" +
                "  }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>\n" +
                "<br>\n" +
                "<br>\n" +
                "<h2>Beste " + person.getName() + "</h2> \n" +
                "<h3>We hebben je perfecte match gevonden!</h3> \n" +
                "<br>\n" +
                "<h3>Klik op onderstaande knop om je match te accepteren op onze officiële website:</h3>\n" +
                "<a href=https://hartverlorenonderdentoren.onrender.com/confirmMatch/" + personId + ">\n" +
                "<button>Jouw match</button>\n" +
                "</a>\n" +
                "<h4>Met vriendelijke groet het hele OnderDenToren team.</h4>" +
                "<br>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    @GetMapping({"/confirmMatch/{personId}"})
    public String confirmMatch(Model model, @PathVariable Integer personId) {
        Optional<Match> match = matchRepository.findMatchByPerson1_Id(personId);
        if (match.isEmpty()) {
            match = matchRepository.findMatchByPerson2_Id(personId);
        }

        if (match.isPresent()) {
            Optional<Person> matchedPerson = personRepository.findById(personId);

            matchedPerson.ifPresent(person -> model.addAttribute("matchedPerson", person));
        }
        model.addAttribute("match", match);
        return "confirmMatch";
    }

    @GetMapping(value = {"/confirmMatchAccept/{id}"})
    public String confirmMatchAccept(@PathVariable Integer id) {
        Optional<Match> matchOptional = matchRepository.findById(id);
        int randomStandNumber = (int) Math.round(1 + (Math.random() * (standRepository.count() - 1)));

        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            if (match.getConfirmationCount() < 0.5) {
                match.setConfirmationCount(0.5);
                logger.info(Double.toString(match.getConfirmationCount()));
            } else if (match.getConfirmationCount() == 0.5) {
                match.setConfirmationCount(1);
                match.setConfirmed(true);
                logger.info(Boolean.toString(match.isConfirmed()));

                Person person1 = matchOptional.get().getPerson1();
                Person person2 = matchOptional.get().getPerson2();
                try {
                    Optional<Stand> randomStand = standRepository.findById(randomStandNumber);
                    if (randomStand.isPresent()) {
                        logger.info("random stand : " + randomStand.get().getName());


                        String bodyTextQRCode1 = mailQRBody(person1, randomStand.get());
                        String bodyTextQRCode2 = mailQRBody(person2, randomStand.get());

                        email(matchOptional.get().getPerson1(), "Je match heeft geaccepteerd!", bodyTextQRCode1, true);
                        email(matchOptional.get().getPerson2(), "Je match heeft geaccepteerd!", bodyTextQRCode2, true);
                    }
                    //send feedback mail
                    String bodyFeedback1 = mailFeedbackBody(person1);
                    String bodyFeedback2 = mailFeedbackBody(person2);

                    TimerTask task = new TimerTask() {
                        public void run() {
                            try {
                                email(person1, "Laat ons weten wat je van de date vond", bodyFeedback1, false);
                                email(person2, "Laat ons weten wat je van de date vond", bodyFeedback2, false);
                            } catch (MessagingException | IOException | WriterException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    Timer timer = new Timer("Timer");
                    long delay = 60000;
                    timer.schedule(task, delay);
                } catch (MessagingException | WriterException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        matchOptional.ifPresent(match -> matchRepository.save(match));


        return "redirect:/index";
    }

    private String mailQRBody(Person person, Stand randomStand) {

        return "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "h2,\n" +
                "h3,\n" +
                "h4 {\n" +
                "   color:rgb(114, 28, 37)\n" +
                "}\n" +
                "div {\n" +
                "   text-align: center;\n" +
                "   background-color: rgb(248,186,189);\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>\n" +
                "<br>\n" +
                "<br>\n" +
                "<h2>Beste " + person.getName() + "</h2>\n" +
                "<h3>Om je date te beginnen wordt je verwacht bij de stand <q>" + randomStand.getName() + "</q>.</h3>\n" +
                "<br>\n" +
                "<h3>In bijlage kan je de QR code vinden voor je gratis drankje,</h3>\n" +
                "<h3>gelieve deze code te laten scannen aan het standje <q>" + randomStand.getName() + "</q> om je drankje te krijgen.</h3>\n" +
                "<br>\n" +
                "<h4>Met vriendelijke groet het hele OnderDenToren team.</h4>\n" +
                "<br>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String mailFeedbackBody(Person person) {

        return "<head> \n" +
                "<style>\n" +
                "  h2,\n" +
                "  h3," +
                "  h4 {\n" +
                "    color:rgb(114, 28, 37)\n" +
                "  }\n" +
                "  button  {\n" +
                "    background-color: #ff4d4d;\n" +
                "    color: #fff;\n" +
                "    border: none;\n" +
                "    border-radius: 8px;\n" +
                "    font-size: 15px;\n" +
                "    font-weight: bold;\n" +
                "    text-transform: uppercase;\n" +
                "    padding: 12px 20px;\n" +
                "    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.3);\n" +
                "    transition: all 0.2s ease-in-out;\n" +
                "  } \n" +
                "  button:hover {\n" +
                "    background-color: #e60000;\n" +
                "    cursor: pointer;\n" +
                "    transform: scale(1.05);\n" +
                "  }\n" +
                "  div {\n" +
                "    text-align: center;\n" +
                "    background-color: rgb(248,186,189);\n" +
                "  }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>" +
                "<br>\n" +
                "<br>\n" +
                "<h2>Beste " + person.getName() + "</h2> \n" +
                "<h3>We zouden het zeer waarderen als u even de tijd zou willen nemen </h3> \n" +
                "<h3>om ons te laten weten wat je van de date en het evenement vond.</h3>\n" +
                "<br>\n" +
                "<h3>Klik op de knop hieronder om ons feedback te kunnen geven op onze officiële site:</h3>\n" +
                "<a href=https://hartverlorenonderdentoren.onrender.com/feedback/" + person.getId() + ">\n" +
                "<button>Feedback</button>\n" +
                "</a>\n" +
                "<h4>Met vriendelijke groet het hele OnderDenToren team.</h4>" +
                "<br>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    @GetMapping(value = {"/confirmMatchDeny/{id}/{matchedPersonId}"})
    public String confirmMatchDeny(@PathVariable Integer id,
                                   @PathVariable Integer matchedPersonId) {
        Optional<Match> matchOptional = matchRepository.findById(id);

        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setConfirmationCount(-1);
            matchRepository.save(match);

            try {
                Optional<Person> person1 = personRepository.findById(matchedPersonId);
                if (person1.isPresent()) {
                    String cancellationMessage1 = mailAnnulationBody(person1.get());
                    email(person1.get(), "Match geannuleerd", cancellationMessage1, false);
                }
            } catch (MessagingException | IOException | WriterException e) {
                throw new RuntimeException(e);
            }
        }

        return "redirect:/index";
    }

    private String mailAnnulationBody(Person person) {
        return "<head> \n" +
                "<style>\n" +
                "  h2,\n" +
                "  h3," +
                "  h4 {\n" +
                "    color:rgb(114, 28, 37)\n" +
                "  }\n" +
                "  button  {\n" +
                "    background-color: #ff4d4d;\n" +
                "    color: #fff;\n" +
                "    border: none;\n" +
                "    border-radius: 8px;\n" +
                "    font-size: 15px;\n" +
                "    font-weight: bold;\n" +
                "    text-transform: uppercase;\n" +
                "    padding: 12px 20px;\n" +
                "    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.3);\n" +
                "    transition: all 0.2s ease-in-out;\n" +
                "  } \n" +
                "  button:hover {\n" +
                "    background-color: #e60000;\n" +
                "    cursor: pointer;\n" +
                "    transform: scale(1.05);\n" +
                "  }\n" +
                "  div {\n" +
                "    text-align: center;\n" +
                "    background-color: rgb(248,186,189);\n" +
                "  }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>" +
                "<br>\n" +
                "<br>\n" +
                "<h2>Beste " + person.getName() + "</h2> \n" +
                "<br>\n" +
                "<h3>Het spijt ons om u mee te delen dat uw match heeft afgezegd,</h3>\n" +
                "<h3>wij wensen u nog veel succes verder.</h3>\n" +
                "<br>" +
                "<h4>Met vriendelijke groet het hele OnderDenToren team.</h4>" +
                "<br>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    @GetMapping({"/matchmaker/overviewPersonWithoutMatch"})
    public String overviewPeople(Model model, @RequestParam(required = false) String name, @RequestParam(required = false) String gender, @RequestParam(required = false) Integer minAge, @RequestParam(required = false)  Integer maxAge, @RequestParam(required = false) Integer maxPersonAmount) {
        if (maxPersonAmount == null) {
            maxPersonAmount = 25;
        }
        if (minAge == null){
            minAge = 0;
        }
        else {
            model.addAttribute("minAge", minAge);
        }
        if (maxAge == null){
            maxAge = 5000;
        }
        else {
            model.addAttribute("maxAge", maxAge);
        }
        if (name != null){
            model.addAttribute("name", name);
        }
        if (gender != null){
            model.addAttribute("gender", gender);
        }
        LocalDate minBirthdate1 = LocalDate.now().minusYears(maxAge);
        LocalDate maxBirthdate2 = LocalDate.now().minusYears(minAge );

        Optional<Event> activeEvent = eventRepository.findByActive();
        if (activeEvent.isPresent()) {
            final Iterable<Person> people = personRepository.findAllByEventAndMatched(activeEvent.get(), false);
            model.addAttribute("people", people);
        }
        final Iterable<Person> personList = personRepository.findByFilter(name, gender, minBirthdate1, maxBirthdate2);
        model.addAttribute("personList", personList);

        model.addAttribute("today", LocalDate.now());
        model.addAttribute("maxPersonAmount", maxPersonAmount);
        model.addAttribute("genders", genderRepository.findAll());
        logger.info("name " + name);
        logger.info("gender " + gender);
        logger.info("minAge " + minAge);
        logger.info("maxAge " + maxAge);


        return "matchmaker/overviewPersonWithoutMatch";
    }

    @PostMapping("/matchmaker/overviewPersonWithoutMatch")
    public String findPotentialMatches(Integer personChosen) {

        return "redirect:/matchmaker/algorithm/" + personChosen;
    }

    public void email(Person person, String subject, String body, Boolean attachment) throws MessagingException, IOException, WriterException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(senderEmail);
        helper.setTo(person.getEmail());
        helper.setSubject(subject);
        helper.setText(body, true);

        if (attachment) {
            String url = "https://hartverlorenonderdentoren.onrender.com/qrCodeCheck/" + person.getId();

            BufferedImage image = QRCodeGenerator.generateQRCodeImage(url, 250, 250);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
            byte[] imageInByte = outputStream.toByteArray();
            ByteArrayDataSource imageAttachment = new ByteArrayDataSource(imageInByte, "application/x-any");
            outputStream.close();

            helper.addAttachment("QRCode.png", imageAttachment);
        }
        javaMailSender.send(message);
        logger.info("Mail has been send!");
    }
}
