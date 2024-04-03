package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String gender;
    private String genderFrench;
    private String genderEnglish;

    public Gender() {
    }

    public Gender(String gender, String genderFrench, String genderEnglish) {
        this.gender = gender;
        this.genderFrench = genderFrench;
        this.genderEnglish = genderEnglish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderFrench() {
        return genderFrench;
    }

    public void setGenderFrench(String genderFrench) {
        this.genderFrench = genderFrench;
    }

    public String getGenderEnglish() {
        return genderEnglish;
    }

    public void setGenderEnglish(String genderEnglish) {
        this.genderEnglish = genderEnglish;
    }

}
