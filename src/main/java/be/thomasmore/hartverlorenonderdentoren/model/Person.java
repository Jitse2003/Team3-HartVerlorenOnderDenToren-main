package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name, email, gender, genderTranslation, crystalBall, crystalBallTranslation, perfectDay, perfectDayTranslation, interests, interestsTranslation, houseOnFire, HouseOnFireTranslation, friendshipMeaning, famous;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private Integer minAgePreference;
    private Integer maxAgePreference;
    private String genderPreference,  genderPreferenceTranslation;

    private String interestsPreference, interestsPreferenceTranslation;

    private boolean matched;
    private String image;
    private boolean qrScanned;
    private LocalDateTime timeAdded;

    @ManyToOne(optional = false)
    private Event event;


    public Person() {
    }

    public Person(String name, String email, LocalDate birthdate, String gender, String crystalBall, String perfectDay, String interests, Event event, String famous) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.crystalBall = crystalBall;
        this.perfectDay = perfectDay;
        this.interests = interests;
        this.birthdate = birthdate;
        this.matched = false;
        this.qrScanned = false;
        this.event = event;
        this.famous = famous;
        this.timeAdded = LocalDateTime.now();
    }

    public Person(Integer id, String name, String email, String gender, String crystalBall, String perfectDay, String houseOnFire, String friendShipMeaning, String famous, String interests, LocalDate birthDate, int minAgePreference, int maxAgePreference, String genderPreference, String interestsPreference, boolean matched, String image, boolean qrScanned, Event event) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.crystalBall = crystalBall;
        this.perfectDay = perfectDay;
        this.interests = interests;
        this.birthdate = birthDate;
        this.minAgePreference = minAgePreference;
        this.maxAgePreference = maxAgePreference;
        this.genderPreference = genderPreference;
        this.interestsPreference = interestsPreference;
        this.matched = matched;
        this.image = image;
        this.qrScanned = qrScanned;
        this.event = event;
        this.houseOnFire = houseOnFire;
        this.famous = famous;
        this.friendshipMeaning = friendShipMeaning;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCrystalBall() {
        return crystalBall;
    }

    public void setCrystalBall(String crystalBall) {
        this.crystalBall = crystalBall;
    }

    public String getPerfectDay() {
        return perfectDay;
    }

    public void setPerfectDay(String hairColor) {
        this.perfectDay = hairColor;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Integer getMinAgePreference() {
        return minAgePreference;
    }

    public void setMinAgePreference(Integer minAgePreference) {
        this.minAgePreference = minAgePreference;
    }

    public Integer getMaxAgePreference() {
        return maxAgePreference;
    }

    public void setMaxAgePreference(Integer maxAgePreference) {
        this.maxAgePreference = maxAgePreference;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public String getInterestsPreference() {
        return interestsPreference;
    }

    public void setInterestsPreference(String interestsPreference) {
        this.interestsPreference = interestsPreference;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isQrScanned() {
        return qrScanned;
    }

    public void setQrScanned(boolean qrScanned) {
        this.qrScanned = qrScanned;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getTimeAdded() {
        return timeAdded;
    }
    public String getGenderTranslation() {
        return genderTranslation;
    }

    public void setGenderTranslation(String genderTranslation) {
        this.genderTranslation = genderTranslation;
    }

    public String getCrystalBallTranslation() {
        return crystalBallTranslation;
    }

    public void setCrystalBallTranslation(String crystalBallTranslation) {
        this.crystalBallTranslation = crystalBallTranslation;
    }

    public String getPerfectDayTranslation() {
        return perfectDayTranslation;
    }

    public void setPerfectDayTranslation(String hairColorTranslation) {
        this.perfectDayTranslation = hairColorTranslation;
    }

    public String getInterestsTranslation() {
        return interestsTranslation;
    }

    public void setInterestsTranslation(String interestsTranslation) {
        this.interestsTranslation = interestsTranslation;
    }

    public String getHouseOnFire() {
        return houseOnFire;
    }

    public void setHouseOnFire(String houseOnFire) {
        this.houseOnFire = houseOnFire;
    }

    public String getHouseOnFireTranslation() {
        return HouseOnFireTranslation;
    }

    public void setHouseOnFireTranslation(String houseOnFireTranslation) {
        HouseOnFireTranslation = houseOnFireTranslation;
    }

    public String getGenderPreferenceTranslation() {
        return genderPreferenceTranslation;
    }

    public void setGenderPreferenceTranslation(String genderPreferenceTranslation) {
        this.genderPreferenceTranslation = genderPreferenceTranslation;
    }

    public String getInterestsPreferenceTranslation() {
        return interestsPreferenceTranslation;
    }

    public void setInterestsPreferenceTranslation(String interestsPreferenceTranslation) {
        this.interestsPreferenceTranslation = interestsPreferenceTranslation;
    }

    public String getFriendshipMeaning() {
        return friendshipMeaning;
    }

    public void setFriendshipMeaning(String friendshipMeaning) {
        this.friendshipMeaning = friendshipMeaning;
    }

    public String getFamous() {
        return famous;
    }

    public void setFamous(String famous) {
        this.famous = famous;
    }

}
