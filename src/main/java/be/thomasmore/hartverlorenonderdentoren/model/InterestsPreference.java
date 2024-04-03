package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;

@Entity
public class InterestsPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String interestsPreference;
    private String interestsPreferenceFrench;
    private String interestsPreferenceEnglish;

    public InterestsPreference() {
    }

    public InterestsPreference(String interestsPreference, String interestsPreferenceFrench, String interestsPreferenceEnglish) {
        this.interestsPreference = interestsPreference;
        this.interestsPreferenceFrench = interestsPreferenceFrench;
        this.interestsPreferenceEnglish = interestsPreferenceEnglish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterestsPreference() {
        return interestsPreference;
    }

    public void setInterestsPreference(String interestsPreference) {
        this.interestsPreference = interestsPreference;
    }

    public String getInterestsPreferenceFrench() {
        return interestsPreferenceFrench;
    }

    public void setInterestsPreferenceFrench(String interestsPreferenceFrench) {
        this.interestsPreferenceFrench = interestsPreferenceFrench;
    }

    public String getInterestsPreferenceEnglish() {
        return interestsPreferenceEnglish;
    }

    public void setInterestsPreferenceEnglish(String interestsPreferenceEnglish) {
        this.interestsPreferenceEnglish = interestsPreferenceEnglish;
    }
}
