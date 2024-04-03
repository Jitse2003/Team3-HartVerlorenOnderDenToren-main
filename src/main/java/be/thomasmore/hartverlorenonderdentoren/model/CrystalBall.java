package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CrystalBall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String crystalBall;
    private String crystalBallFrench;
    private String crystalBallEnglish;

    public CrystalBall() {
    }

    public CrystalBall(String crystalBall, String crystalBallFrench, String crystalBallEnglish) {
        this.crystalBall = crystalBall;
        this.crystalBallFrench = crystalBallFrench;
        this.crystalBallEnglish = crystalBallEnglish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCrystalBall() {
        return crystalBall;
    }

    public void setCrystalBall(String crystalBall) {
        this.crystalBall = crystalBall;
    }

    public String getCrystalBallFrench() {
        return crystalBallFrench;
    }

    public void setCrystalBallFrench(String crystalBallFrench) {
        this.crystalBallFrench = crystalBallFrench;
    }

    public String getCrystalBallEnglish() {
        return crystalBallEnglish;
    }

    public void setCrystalBallEnglish(String crystalBallEnglish) {
        this.crystalBallEnglish = crystalBallEnglish;
    }
}
