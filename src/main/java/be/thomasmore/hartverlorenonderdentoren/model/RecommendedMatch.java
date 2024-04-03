package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;

@Entity
public class RecommendedMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person1;
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person2;

    private Integer percentage;

    public RecommendedMatch() {
    }

    public RecommendedMatch(Person person1, Person person2, Integer percentage) {
        this.person1 = person1;
        this.person2 = person2;
        this.percentage = percentage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
