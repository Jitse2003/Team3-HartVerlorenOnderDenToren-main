package be.thomasmore.hartverlorenonderdentoren.controllers;


import be.thomasmore.hartverlorenonderdentoren.model.*;
import be.thomasmore.hartverlorenonderdentoren.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SettingsController {

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private InterestsRepository interestsRepository;

    @Autowired
    InterestsPreferenceRepository interestsPreferenceRepository;

    @Autowired
    private HouseOnFireRepository houseOnFireRepository;

    @Autowired
    private PerfectDayRepository perfectDayRepository;

    @Autowired
    private CrystalBallRepo crystalBallRepo;

    @GetMapping("/admin/settingPage")
    public String settingsPage(Model model) {

        model.addAttribute("houseOnFires", houseOnFireRepository.findAll());
        model.addAttribute("genders", genderRepository.findAll());
        model.addAttribute("perfectDays", perfectDayRepository.findAll());
        model.addAttribute("interests", interestsRepository.findAll());
        model.addAttribute("interestsPreferences", interestsPreferenceRepository.findAll());
        model.addAttribute("crystalBalls", crystalBallRepo.findAll());

        return "admin/settingPage";
    }

    @PostMapping("/admin/addHouseOnFireForm")
    public String addHouseOnFireForm(@RequestParam("addHouseOnFire") String addHouseOnFire, @RequestParam("addHouseOnFireFrench") String addHouseOnFireFrench, @RequestParam("addHouseOnFireEnglish") String addHouseOnFireEnglish) {
        if (addHouseOnFire != null && !addHouseOnFire.isEmpty() && addHouseOnFireFrench != null && !addHouseOnFireFrench.isEmpty() && addHouseOnFireEnglish != null && !addHouseOnFireEnglish.isEmpty()) {
            houseOnFireRepository.save(new HouseOnFire(addHouseOnFire,addHouseOnFireFrench,addHouseOnFireEnglish));
        }
        return "redirect:settingPage";
    }

    @PostMapping("/admin/addGenderForm")
    public String addGenderForm(@RequestParam("addGender") String addGender, @RequestParam("addGenderFrench") String addGenderFrench, @RequestParam("addGenderEnglish") String addGenderEnglish) {
        if (addGender != null && !addGender.isEmpty() && addGenderFrench != null && !addGenderFrench.isEmpty() && addGenderEnglish != null && !addGenderEnglish.isEmpty()) {
            genderRepository.save(new Gender(addGender,addGenderFrench,addGenderEnglish));
        }

        return "redirect:settingPage";
    }

    @PostMapping("/admin/addCrystalBallForm")
    public String addCrystalBallForm(@RequestParam("addCrystalBall") String addCrystalBall, @RequestParam("addCrystalBallFrench") String addCrystalBallFrench, @RequestParam("addCrystalBallEnglish") String addCrystalBallEnglish) {
        if (addCrystalBall != null && !addCrystalBall.isEmpty() && addCrystalBallFrench != null && !addCrystalBallFrench.isEmpty() && !addCrystalBallEnglish.isEmpty()) {
            crystalBallRepo.save(new CrystalBall(addCrystalBall,addCrystalBallFrench,addCrystalBallEnglish));
        }

        return "redirect:settingPage";
    }

    @PostMapping("/admin/addInterestForm")
    public String addInterestForm(@RequestParam("addInterest") String addInterest,@RequestParam("addInterestFrench") String addInterestFrench,@RequestParam("addInterestEnglish") String addInterestEnglish) {
        if (addInterest != null && !addInterest.isEmpty() && addInterestFrench != null && !addInterestFrench.isEmpty() && addInterestEnglish != null && !addInterestEnglish.isEmpty()) {
            interestsRepository.save(new Interests(addInterest,addInterestFrench,addInterestEnglish));
        }

        return "redirect:settingPage";
    }

    @PostMapping("/admin/addInterestPreferenceForm")
    public String addInterestPreferenceForm(@RequestParam("addInterestPreference") String addInterestPreference,@RequestParam("addInterestPreferenceFrench") String addInterestPreferenceFrench,@RequestParam("addInterestPreferenceEnglish") String addInterestPreferenceEnglish) {
        if (addInterestPreference != null && !addInterestPreference.isEmpty() && addInterestPreferenceFrench != null && !addInterestPreferenceFrench.isEmpty() && addInterestPreferenceEnglish!=null && !addInterestPreferenceEnglish.isEmpty()) {
            interestsPreferenceRepository.save(new InterestsPreference(addInterestPreference,addInterestPreferenceFrench,addInterestPreferenceEnglish));
        }

        return "redirect:settingPage";
    }

    @PostMapping("/admin/addPerfectDayForm")
    public String addPerfectDayForm(@RequestParam("addPerfectDay") String addPerfectDay,@RequestParam("addPerfectDayFrench") String addPerfectDayFrench,@RequestParam("addPerfectDayEnglish") String addPerfectDayEnglish) {
        if (addPerfectDay != null && !addPerfectDay.isEmpty() && addPerfectDayFrench != null && !addPerfectDayFrench.isEmpty() && addPerfectDayEnglish!=null && !addPerfectDayEnglish.isEmpty()) {
            perfectDayRepository.save(new PerfectDay(addPerfectDay,addPerfectDayFrench,addPerfectDayEnglish));
        }

        return "redirect:settingPage";
    }

    @Transactional
    @PostMapping("/admin/deleteHouseOnFireForm")
    public String deleteHairColorForm(@RequestParam("deleteHouseOnFire") String deleteHouseOnFire) {
        houseOnFireRepository.deleteByHouseOnFire(deleteHouseOnFire);
        return "redirect:settingPage";
    }

    @Transactional
    @PostMapping("/admin/deleteGenderForm")
    public String deleteGenderForm(@RequestParam("deleteGender") String deleteGender) {
        genderRepository.deleteByGender(deleteGender);

        return "redirect:settingPage";
    }

    @Transactional
    @PostMapping("/admin/deleteCrystalBallForm")
    public String deleteEyeColorForm(@RequestParam("deleteCrystalBall") String deleteCrystalBall) {
        crystalBallRepo.deleteByCrystalBall(deleteCrystalBall);

        return "redirect:settingPage";
    }

    @Transactional
    @PostMapping("/admin/deleteInterestForm")
    public String deleteInterestForm(@RequestParam("deleteInterest") String deleteInterest) {
        interestsRepository.deleteByInterests(deleteInterest);

        return "redirect:settingPage";
    }

    @Transactional
    @PostMapping("/admin/deleteInterestPreferenceForm")
    public String deleteInterestPreferenceForm(@RequestParam("deleteInterestPreference") String deleteInterestPreference) {
        interestsPreferenceRepository.deleteByInterestsPreference(deleteInterestPreference);

        return "redirect:settingPage";
    }

    @Transactional
    @PostMapping("/admin/deletePerfectDayForm")
    public String deletePerfectDayForm(@RequestParam("deletePerfectDay") String deletePerfectDay) {
        perfectDayRepository.deleteByPerfectDay(deletePerfectDay);

        return "redirect:settingPage";
    }

}
