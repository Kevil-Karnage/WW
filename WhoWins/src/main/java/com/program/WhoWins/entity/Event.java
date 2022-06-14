package com.program.WhoWins.entity;

import javax.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    // @Column(name = "id", updatable = false, nullable = false, unique=true)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", updatable = false, nullable = false, unique = true)
    private String name;

    public Event(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
