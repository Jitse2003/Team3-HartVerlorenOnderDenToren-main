package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HouseOnFire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String houseOnFire;
    private String houseOnFireFrench;
    private String houseOnFireEnglish;

    public HouseOnFire(){
    }

    public HouseOnFire(String houseOnFire, String houseOnFireFrench, String houseOnFireEnglish) {
        this.houseOnFire = houseOnFire;
        this.houseOnFireFrench = houseOnFireFrench;
        this.houseOnFireEnglish = houseOnFireEnglish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHouseOnFire() {
        return houseOnFire;
    }

    public void setHouseOnFire(String houseOnFire) {
        this.houseOnFire = houseOnFire;
    }

    public String getHouseOnFireFrench() {
        return houseOnFireFrench;
    }

    public void setHouseOnFireFrench(String houseOnFireFrench) {
        this.houseOnFireFrench = houseOnFireFrench;
    }

    public String getHouseOnFireEnglish() {
        return houseOnFireEnglish;
    }

    public void setHouseOnFireEnglish(String houseOnFireEnglish) {
        this.houseOnFireEnglish = houseOnFireEnglish;
    }
}
