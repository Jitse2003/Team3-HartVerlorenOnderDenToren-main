package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    private String address;

    private String naamVerantwoordelijke;

    private String telefoonnummer;

    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Event> event;

    @OneToMany
    private Collection<Stand> stand;
    
    public Sponsor() {
    }

    public Sponsor(String name, String address, String naamVerantwoordelijke, String telefoonnummer, Collection<Event> event) {
        this.name = name;
        this.address = address;
        this.naamVerantwoordelijke = naamVerantwoordelijke;
        this.telefoonnummer = telefoonnummer;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNaamVerantwoordelijke() {
        return naamVerantwoordelijke;
    }

    public void setNaamVerantwoordelijke(String naamVerantwoordelijke) {
        this.naamVerantwoordelijke = naamVerantwoordelijke;
    }

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public void setTelefoonnummer(String telefoonnummer) {
        this.telefoonnummer = telefoonnummer;
    }

    public Collection<Event> getEvent() {
        return event;
    }

    public void setEvent(Collection<Event> event) {
        this.event = event;
    }

    public Collection<Stand> getStand() {
        return stand;
    }

    public void setStand(Collection<Stand> stand) {
        this.stand = stand;
    }
}
