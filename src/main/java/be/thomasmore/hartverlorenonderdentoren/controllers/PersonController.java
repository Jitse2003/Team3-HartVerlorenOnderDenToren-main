package be.thomasmore.hartverlorenonderdentoren.controllers;

import be.thomasmore.hartverlorenonderdentoren.model.*;
import be.thomasmore.hartverlorenonderdentoren.repositories.*;
import be.thomasmore.hartverlorenonderdentoren.utils.GoogleService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Controller
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(MatchesController.class);

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    InterestsRepository interestsRepository;

    @Autowired
    InterestsPreferenceRepository interestsPreferenceRepository;

    @Autowired
    PerfectDayRepository perfectDayRepository;

    @Autowired
    CrystalBallRepo crystalBallRepo;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    HouseOnFireRepository houseOnFireRepository;

    @Autowired
    private GoogleService googleService;

    @GetMapping("/matchmaker/personDetails")
    public String personDetails(Model model) {
        Optional<Person> people = personRepository.findById(1);
        people.ifPresent(person -> model.addAttribute("people", person));
        return "matchmaker/personDetails";
    }

    @GetMapping({"/personalInformationForm"})
    public String form(Model model) {

        List<Interests> interesses = interestsRepository.findAll();
        model.addAttribute("interests", interesses);
        model.addAttribute("perfectDays", perfectDayRepository.findAll());
        model.addAttribute("crystalBall", crystalBallRepo.findAll());
        model.addAttribute("genders", genderRepository.findAll());
        model.addAttribute("today", LocalDate.now().minusYears(18));
        logger.info("interesses: " + interesses);

        return "personalInformationForm";
    }

    @GetMapping({"/preferencePage/{id}"})
    public String preferencePage(Model model, @PathVariable Integer id) {

        List<InterestsPreference> interestsPreferences = (List<InterestsPreference>) interestsPreferenceRepository.findAll();
        logger.info("interests preferences: " + interestsPreferences);

        model.addAttribute("interestsPreferences", interestsPreferences);
        model.addAttribute("genders", genderRepository.findAll());
        model.addAttribute("houseOnFires", houseOnFireRepository.findAll());
        model.addAttribute("id", id);

        return "preferencePage";
    }

    @GetMapping("confirmData/{id}")
    public String confirmPersonData(Model model, @PathVariable Integer id) {
        addPersonData(model, id);

        return "confirmData";
    }

    @GetMapping("editData/{id}")
    public String editPersonData(Model model, @PathVariable Integer id) {
        addPersonData(model, id);

        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            List<String> convertedInterests = Stream.of(person.get().getInterests().split(", ", -1)).toList();
            model.addAttribute("convertedInterests", convertedInterests);
            List<String> convertedInterestsPreferences = Stream.of(person.get().getInterestsPreference().split(", ", -1)).toList();
            model.addAttribute("convertedInterestsPreferences", convertedInterestsPreferences);

            for (String i : convertedInterests) {
                logger.info("convertedInterests: /" + i + "/");
            }
            for (String i : convertedInterestsPreferences) {
                logger.info("convertedInterestsPreferences: /" + i + "/");
            }
        }

        return "editData";
    }

    @PostMapping("editData/{id}")
    public String savePersonDataChanges(@RequestParam("language") String language, @PathVariable Integer id,
                                        String name,
                                        String email,
                                        LocalDate birthdate,
                                        Integer gender,
                                        Integer crystalBall,
                                        Integer perfectDay,
                                        String famous,
                                        String interests,
                                        Integer minAge,
                                        Integer maxAge,
                                        Integer genderPreference,
                                        Integer houseOnFire,
                                        String interestsPreference,
                                        String friendshipMeaning) {

        List<Integer> interestsIds = new ArrayList<>();
        for (String s : interests.split(",")) {
            interestsIds.add(Integer.parseInt(s));
        }

        List<Integer> interestsPreferenceIds = new ArrayList<>();
        for (String s : interestsPreference.split(",")) {
            interestsPreferenceIds.add(Integer.parseInt(s));
        }

        Optional<Gender> genderObj = genderRepository.findById(gender);
        Optional<CrystalBall> crystalBallOptional = crystalBallRepo.findById(crystalBall);
        Optional<PerfectDay> perfectDayOptional = perfectDayRepository.findById(perfectDay);
        Optional<Gender> genderPreferenceObj = genderRepository.findById(genderPreference);
        Optional<HouseOnFire> houseOnFireOptional = houseOnFireRepository.findById(houseOnFire);
        List<Interests> interestsList = interestsRepository.findAllByIds(interestsIds);
        List<InterestsPreference> interestsPreferenceList = interestsPreferenceRepository.findAllByIds(interestsPreferenceIds);

        if (personRepository.findById(id).isPresent() && genderObj.isPresent() && crystalBallOptional.isPresent() && perfectDayOptional.isPresent()
                && genderPreferenceObj.isPresent() && houseOnFireOptional.isPresent()) {
            Person person = personRepository.findById(id).get();
            person.setName(name);
            person.setEmail(email);
            person.setBirthdate(birthdate);
            person.setGender(genderObj.get().getGender());
            person.setCrystalBall(crystalBallOptional.get().getCrystalBall());
            person.setPerfectDay(perfectDayOptional.get().getPerfectDay());
            person.setFamous(famous);
            person.setMinAgePreference(minAge);
            person.setMaxAgePreference(maxAge);
            person.setGenderPreference(genderPreferenceObj.get().getGender());
            person.setHouseOnFire(houseOnFireOptional.get().getHouseOnFire());
            person.setFriendshipMeaning(friendshipMeaning);

            saveInterestsTranslation(interestsList, language, person, genderObj.get(), crystalBallOptional.get(), perfectDayOptional.get());
            saveInterestsPreferenceTranslation(interestsPreferenceList, language, person, genderPreferenceObj.get(), houseOnFireOptional.get());

            personRepository.save(person);
        }

        return "redirect:/confirmData/" + id;
    }


    private void addPersonData(Model model, @PathVariable Integer id) {
        Optional<Person> person = personRepository.findById(id);

        person.ifPresent(value -> model.addAttribute("person", value));

        model.addAttribute("interests", interestsRepository.findAll());
        model.addAttribute("interestsPreferences", interestsPreferenceRepository.findAll());
        model.addAttribute("perfectDays", perfectDayRepository.findAll());
        model.addAttribute("crystalBall", crystalBallRepo.findAll());
        model.addAttribute("genders", genderRepository.findAll());
        model.addAttribute("houseOnFires", houseOnFireRepository.findAll());
    }

    @PostMapping({"/addPersonData"})
    public String addPersonData(@RequestParam("language") String language, String name, String email, LocalDate birthdate, Integer genderId, Integer crystalBallId, Integer perfectDayId, String interestsId, String famous) {
        Optional<Event> eventOptional = eventRepository.findByActive();
        Event event = new Event();

        if (eventOptional.isPresent()) {
            event = eventOptional.get();
        }

        List<Integer> interestsIds = new ArrayList<>();
        for (String s : interestsId.split(",")) {
            interestsIds.add(Integer.parseInt(s));
        }

        Optional<Gender> gender = genderRepository.findById(genderId);
        Optional<PerfectDay> perfectDay = perfectDayRepository.findById(perfectDayId);
        Optional<CrystalBall> crystalBall = crystalBallRepo.findById(crystalBallId);
        List<Interests> interests = interestsRepository.findAllByIds(interestsIds);


        StringBuilder interestValue = new StringBuilder();
        for (Interests interest : interests) {
            interestValue.append(interest.getInterests()).append(", ");
        }
        interestValue.deleteCharAt(interestValue.length() - 2);
        interestValue.deleteCharAt(interestValue.length() - 1);


        Person person = new Person();
        if (gender.isPresent() && crystalBall.isPresent() && perfectDay.isPresent()) {
            person = new Person(name, email, birthdate, gender.get().getGender(), crystalBall.get().getCrystalBall(), perfectDay.get().getPerfectDay(), interestValue.toString(), event, famous);

        }

        if (gender.isPresent() && perfectDay.isPresent() && crystalBall.isPresent()) {
            saveInterestsTranslation(interests, language, person, gender.get(), crystalBall.get(), perfectDay.get());
        }
        personRepository.save(person);


        logger.info("New person has been added!");
        logger.info("Name: " + name);
        logger.info("Email: " + email);
        logger.info("Birthdate: " + birthdate);
        logger.info("Gender: " + gender);
        logger.info("CrystalBall: " + crystalBall);
        logger.info("PerfectDay: " + perfectDay);
        logger.info("Interests: " + interestsId);
        logger.info("Language: " + language);
        logger.info("famousInput: " + famous);

        return "redirect:/preferencePage/" + person.getId() + "?language=" + language;
    }


    @PostMapping("/addPersonPreferences/{id}")
    public String addPersonPreferences(@RequestParam("language") String language, @PathVariable("id") Integer id,
                                       Integer minAge,
                                       Integer maxAge,
                                       Integer genderPreferenceId,
                                       String interestsPreferenceId,
                                       String friendShipMeaning,
                                       Integer houseOnFireId) {

        List<Integer> interestsPreferenceIds = new ArrayList<>();
        for (String s : interestsPreferenceId.split(",")) {
            interestsPreferenceIds.add(Integer.parseInt(s));
        }
        logger.info("interestPreferenceIds: " + interestsPreferenceIds);


        Optional<Person> person = personRepository.findById(id);

        Optional<Event> event = eventRepository.findByActive();

        Optional<Gender> genderPref = genderRepository.findById(genderPreferenceId);

        Optional<HouseOnFire> houseOnFire = houseOnFireRepository.findById(houseOnFireId);

        List<InterestsPreference> interestsPrefIds = interestsPreferenceRepository.findAllByIds(interestsPreferenceIds);


        StringBuilder interestPreferenceValue = new StringBuilder();
        for (InterestsPreference interestPreference : interestsPrefIds) {
            interestPreferenceValue.append(interestPreference.getInterestsPreference()).append(", ");
        }
        interestPreferenceValue.deleteCharAt(interestPreferenceValue.length() - 2);
        interestPreferenceValue.deleteCharAt(interestPreferenceValue.length() - 1);

        if (person.isPresent() && event.isPresent() && genderPref.isPresent() && !interestsPrefIds.isEmpty() && houseOnFire.isPresent() && !friendShipMeaning.isEmpty()) {
            person.get().setMinAgePreference(minAge);
            person.get().setMaxAgePreference(maxAge);
            person.get().setInterestsPreference(String.valueOf(interestPreferenceValue));
            person.get().setHouseOnFire(houseOnFire.get().getHouseOnFire());
            person.get().setFriendshipMeaning(friendShipMeaning);
            saveInterestsPreferenceTranslation(interestsPrefIds, language, person.get(), genderPref.get(), houseOnFire.get());

            logger.info("" + event.get().getName());
            eventRepository.save(event.get());
        }


        return "redirect:/makePhoto/" + id + "?language=" + language;

    }

    @GetMapping("/makePhoto/{id}")
    public String makePhoto(Model model, @PathVariable Integer id) {
        Optional<Person> person = personRepository.findById(id);
        person.ifPresent(value -> model.addAttribute("person", value));

        return "makePhoto";
    }

    @PostMapping("/addPhoto/{id}")
    public String addPhoto(@RequestParam("dataUrl") String dataUrl, @RequestParam("language") String language, @PathVariable Integer id) {

        String fileName = "image" + id + ".png";
        logger.info("file name: " + fileName);

        String encodingPrefix = "base64,";
        int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
        byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));

        try {
            File imageFile = new File(fileName);
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.close();

            Optional<Person> optionalPerson = personRepository.findById(id);
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                person.setImage(googleService.toFirebase(imageFile, fileName));
                personRepository.save(person);
            }

            if (imageFile.delete()) {
                logger.info("file has successfully been deleted!");
            }

            logger.info("Upload is a success!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Problem with upload! : " + e.getMessage());
        }

        return "redirect:/confirmData/" + id + "?language=" + language;
    }

    private void saveInterestsPreferenceTranslation(List<InterestsPreference> interestsPreferenceList, String language, Person person, Gender gender, HouseOnFire houseOnFire) {
        if (language.equals("en")) {
            StringBuilder interestPreferenceValueEn = new StringBuilder();
            for (InterestsPreference interestPreference : interestsPreferenceList) {
                interestPreferenceValueEn.append(interestPreference.getInterestsPreferenceEnglish()).append(", ");
            }
            interestPreferenceValueEn.deleteCharAt(interestPreferenceValueEn.length() - 2);
            interestPreferenceValueEn.deleteCharAt(interestPreferenceValueEn.length() - 1);

            person.setGenderPreferenceTranslation(gender.getGenderEnglish());
            person.setInterestsPreferenceTranslation(String.valueOf(interestPreferenceValueEn));
            person.setHouseOnFireTranslation(houseOnFire.getHouseOnFireEnglish());
        } else if (language.equals("fr")) {
            StringBuilder interestPreferenceValueFr = new StringBuilder();
            for (InterestsPreference interestPreference : interestsPreferenceList) {
                interestPreferenceValueFr.append(interestPreference.getInterestsPreferenceFrench()).append(", ");
            }
            interestPreferenceValueFr.deleteCharAt(interestPreferenceValueFr.length() - 2);
            interestPreferenceValueFr.deleteCharAt(interestPreferenceValueFr.length() - 1);

            person.setGenderPreferenceTranslation(gender.getGenderFrench());
            person.setInterestsPreferenceTranslation(String.valueOf(interestPreferenceValueFr));
            person.setHouseOnFireTranslation(houseOnFire.getHouseOnFireFrench());
        } else {
            StringBuilder interestPreferenceStrings = new StringBuilder();
            for (InterestsPreference interestPreference : interestsPreferenceList) {
                interestPreferenceStrings.append(interestPreference.getInterestsPreference()).append(", ");
            }
            interestPreferenceStrings.deleteCharAt(interestPreferenceStrings.length() - 2);
            interestPreferenceStrings.deleteCharAt(interestPreferenceStrings.length() - 1);


            person.setGenderPreferenceTranslation(gender.getGender());
            person.setInterestsPreferenceTranslation(String.valueOf(interestPreferenceStrings));
            person.setHouseOnFireTranslation(houseOnFire.getHouseOnFire());
        }
        StringBuilder interestPreferenceValue = new StringBuilder();
        for (InterestsPreference interestPreference : interestsPreferenceList) {
            interestPreferenceValue.append(interestPreference.getInterestsPreference()).append(", ");
        }
        interestPreferenceValue.deleteCharAt(interestPreferenceValue.length() - 2);
        interestPreferenceValue.deleteCharAt(interestPreferenceValue.length() - 1);
        person.setInterestsPreference(String.valueOf(interestPreferenceValue));
    }

    private void saveInterestsTranslation(List<Interests> interestsList, String language, Person person, Gender gender, CrystalBall crystalBall, PerfectDay perfectDay) {
        if (language.equals("en")) {
            StringBuilder interestValueEn = new StringBuilder();
            for (Interests interest : interestsList) {
                interestValueEn.append(interest.getInterestsEnglish()).append(", ");
            }
            interestValueEn.deleteCharAt(interestValueEn.length() - 2);
            interestValueEn.deleteCharAt(interestValueEn.length() - 1);

            person.setGenderTranslation(gender.getGenderEnglish());
            person.setInterestsTranslation(String.valueOf(interestValueEn));
            person.setCrystalBallTranslation(crystalBall.getCrystalBallEnglish());
            person.setPerfectDayTranslation(perfectDay.getPerfectDayEnglish());
        } else if (language.equals("fr")) {
            StringBuilder interestValueFr = new StringBuilder();
            for (Interests interest : interestsList) {
                interestValueFr.append(interest.getInterestsFrench()).append(", ");
            }
            interestValueFr.deleteCharAt(interestValueFr.length() - 2);
            interestValueFr.deleteCharAt(interestValueFr.length() - 1);


            person.setGenderTranslation(gender.getGenderFrench());
            person.setInterestsTranslation(String.valueOf(interestValueFr));
            person.setCrystalBallTranslation(crystalBall.getCrystalBallFrench());
            person.setPerfectDayTranslation(perfectDay.getPerfectDayFrench());
        } else {
            StringBuilder interestValue = new StringBuilder();
            for (Interests interest : interestsList) {
                interestValue.append(interest.getInterests()).append(", ");
            }
            interestValue.deleteCharAt(interestValue.length() - 2);
            interestValue.deleteCharAt(interestValue.length() - 1);


            StringBuilder interestStrings = new StringBuilder();
            for (Interests interest : interestsList) {
                interestStrings.append(interest.getInterests()).append(", ");
            }
            interestStrings.deleteCharAt(interestStrings.length() - 2);
            interestStrings.deleteCharAt(interestStrings.length() - 1);


            person.setGenderTranslation(gender.getGender());
            person.setInterestsTranslation(String.valueOf(interestStrings));

            person.setInterests(String.valueOf(interestValue));
            person.setCrystalBallTranslation(crystalBall.getCrystalBall());
            person.setPerfectDayTranslation(perfectDay.getPerfectDay());
        }
        StringBuilder interestValue = new StringBuilder();
        for (Interests interests : interestsList) {
            interestValue.append(interests.getInterests()).append(", ");
        }
        interestValue.deleteCharAt(interestValue.length() - 2);
        interestValue.deleteCharAt(interestValue.length() - 1);

        person.setInterests(String.valueOf(interestValue));
    }
}
