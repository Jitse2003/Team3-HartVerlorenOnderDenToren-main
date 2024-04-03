package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Boolean selected;

    @ManyToMany
    private Collection<Sponsor> sponsors;

    @OneToMany
    private Collection<Stand> stand;

    public Event() {
    }

    public Event(String name, LocalDate date, Boolean selected) {
        this.name = name;
        this.date = date;
        this.selected = selected;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Collection<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(Collection<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public Collection<Stand> getStand() {
        return stand;
    }

    public void setStand(Collection<Stand> stand) {
        this.stand = stand;
    }
}
