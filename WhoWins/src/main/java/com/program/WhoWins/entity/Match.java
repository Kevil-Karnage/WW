package com.program.WhoWins.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hltv_id", updatable = false, nullable = false)
    private Long hltvId;
    @Column(name = "team_1_id", updatable = false, nullable = false)
    private Long idTeam1;
    @Column(name = "team_2_id", updatable = false, nullable = false)
    private Long idTeam2;
    @Column(name = "event_id", updatable = false, nullable = false)
    private Long eventId;
    @Column(name = "type", updatable = false, nullable = false)
    private int type;
    @Column(name = "date", updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "stars", updatable = false, nullable = false)
    private int stars;
    @Column(name = "position_HLTV_1", updatable = false, nullable = false)
    private int positionHLTV1;
    @Column(name = "position_HLTV_2", updatable = false, nullable = false)
    private int positionHLTV2;
    @Column(name = "ended", updatable = true, nullable = false)
    private boolean ended;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;
        return this.hltvId.equals(match.hltvId);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", hltvId=" + hltvId +
                ", idTeam1=" + idTeam1 +
                ", PositionHLTV1=" + positionHLTV1 +
                ", PositionHLTV2=" + positionHLTV2 +
                ", idTeam2=" + idTeam2 +
                ", eventId=" + eventId +
                ", type=" + type +
                ", date=" + date +
                ", stars=" + stars +
                ", ended=" + ended +
                '}';
    }
}
