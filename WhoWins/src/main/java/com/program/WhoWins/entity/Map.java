package com.program.WhoWins.entity;

import javax.persistence.*;

@Entity
@Table(name = "maps")
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", updatable = false, nullable = false, unique = true)
    private String name;

    public Map(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Map() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return "Map{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
