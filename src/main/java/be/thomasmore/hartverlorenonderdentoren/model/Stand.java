package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;


@Entity
public class Stand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String nameResponsiblePerson;
    private String info;
    @ManyToOne
    private Sponsor sponsor;
    @ManyToOne
    private Event event;

    public Stand() {
    }

    public Stand(String name, String nameResponsiblePerson, String info, Sponsor sponsor, Event event) {
        this.name = name;
        this.nameResponsiblePerson = nameResponsiblePerson;
        this.info = info;
        this.sponsor = sponsor;
        this.event = event;
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

    public String getNameResponsiblePerson() {
        return nameResponsiblePerson;
    }

    public void setNameResponsiblePerson(String nameResponsiblePerson) {
        this.nameResponsiblePerson = nameResponsiblePerson;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String category) {
        this.info = category;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
