package com.program.WhoWins.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "map_types")
public class MapType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", updatable = false, nullable = false, unique = true)
    private String name;
    @Column(name = "actual", updatable = false, nullable = false, unique = false)
    private boolean actual;

    @Override
    public String toString() {
        return "MapType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
