package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PerfectDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String perfectDay;
    private String perfectDayFrench;
    private String perfectDayEnglish;

    public PerfectDay() {
    }

    public PerfectDay(String perfectDay, String perfectDayFrench, String perfectDayEnglish) {
        this.perfectDay = perfectDay;
        this.perfectDayFrench = perfectDayFrench;
        this.perfectDayEnglish = perfectDayEnglish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPerfectDay() {
        return perfectDay;
    }

    public void setPerfectDay(String perfectDay) {
        this.perfectDay = perfectDay;
    }

    public String getPerfectDayFrench() {
        return perfectDayFrench;
    }

    public void setPerfectDayFrench(String perfectDayFrench) {
        this.perfectDayFrench = perfectDayFrench;
    }

    public String getPerfectDayEnglish() {
        return perfectDayEnglish;
    }

    public void setPerfectDayEnglish(String perfectDayEnglish) {
        this.perfectDayEnglish = perfectDayEnglish;
    }
}

