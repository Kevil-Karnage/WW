package com.program.WhoWins.entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "plays_in")
public class PlaysIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "team_id", updatable = false, nullable = false, unique = true)
    private Long teamId;
    @Column(name = "player_id", updatable = false, nullable = false, unique = true)
    private Long playerId;
    @Column(name = "begin_date", updatable = false, nullable = false, unique = true)
    private Date beginDate;
    @Column(name = "end_date", updatable = false, nullable = false, unique = true)
    private Date endDate;

    @Override
    public String toString() {
        return "PlaysIn{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", playerId=" + playerId +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}';
    }
}
