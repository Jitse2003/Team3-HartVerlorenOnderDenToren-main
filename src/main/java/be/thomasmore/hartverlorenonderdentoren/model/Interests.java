package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;

@Entity
public class Interests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String interests;
    private String interestsFrench;
    private String interestsEnglish;

    public Interests() {
    }

    public Interests(String interests, String interestsFrench, String interestsEnglish) {
        this.interests = interests;
        this.interestsFrench = interestsFrench;
        this.interestsEnglish = interestsEnglish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interesse) {
        this.interests = interesse;
    }

    public String getInterestsFrench() {
        return interestsFrench;
    }

    public void setInterestsFrench(String interestsFrench) {
        this.interestsFrench = interestsFrench;
    }

    public String getInterestsEnglish() {
        return interestsEnglish;
    }

    public void setInterestsEnglish(String interestsEnglish) {
        this.interestsEnglish = interestsEnglish;
    }
}
