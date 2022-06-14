package com.program.WhoWins.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTeam1() {
        return idTeam1;
    }

    public void setIdTeam1(Long idTeam1) {
        this.idTeam1 = idTeam1;
    }

    public Long getIdTeam2() {
        return idTeam2;
    }

    public void setIdTeam2(Long idTeam2) {
        this.idTeam2 = idTeam2;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getType() {
        return type;
    }

    public void setType(int countMaps) {
        this.type = countMaps;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Long getId() {
        return id;
    }

    public int getPositionHLTV1() {
        return positionHLTV1;
    }

    public void setPositionHLTV1(int positionHLTV1) {
        this.positionHLTV1 = positionHLTV1;
    }

    public int getPositionHLTV2() {
        return positionHLTV2;
    }

    public void setPositionHLTV2(int positionHLTV2) {
        this.positionHLTV2 = positionHLTV2;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return
                ((idTeam1.equals(match.idTeam1) && idTeam2.equals(match.idTeam2)) ||
                        (idTeam1.equals(match.idTeam2) && idTeam2.equals(match.idTeam1)))  &&
                eventId.equals(match.eventId) &&
                date.getTime() == match.date.getTime();
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTeam1, idTeam2, eventId, date);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
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
