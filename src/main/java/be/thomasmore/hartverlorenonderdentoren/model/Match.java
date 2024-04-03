package be.thomasmore.hartverlorenonderdentoren.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    private Person person1;
    @ManyToOne()
    private Person person2;
    private boolean confirmed;

    @ManyToOne
    private Event event;
    private double confirmationCount;

    private LocalTime timeAdded;

    public Match() {
    }

    public Match(Person person1, Person person2, boolean confirmed, LocalTime timeAdded, Event event) {
        this.person1 = person1;
        this.person2 = person2;
        this.confirmed = confirmed;
        this.confirmationCount = 0.0;
        this.timeAdded = timeAdded;
        this.event = event;
    }

    public double getConfirmationCount() {
        return confirmationCount;
    }

    public void setConfirmationCount(double confirmationCount) {
        this.confirmationCount = confirmationCount;
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

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public LocalTime getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(LocalTime timeAdded) {
        this.timeAdded = timeAdded;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
