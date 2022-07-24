package com.program.WhoWins.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", updatable = false, nullable = false, unique = true)
    private String name;
    @Column(name = "begin_date", updatable = false, nullable = false)
    private Date beginDate;
    @Column(name = "end_date", updatable = false, nullable = false)
    private Date endDate;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", begin_date='" + beginDate + "'" +
                ", end_date='" + endDate + "'" +
                '}';
    }
}
