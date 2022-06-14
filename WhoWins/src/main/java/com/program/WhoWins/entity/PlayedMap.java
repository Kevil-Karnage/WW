package com.program.WhoWins.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "played_maps")
public class PlayedMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "match_id", updatable = false, nullable = false)
    private long matchId;
    @Column(name = "map_id", updatable = false, nullable = false)
    private long mapId;
    @Column(name = "score_team_1", updatable = false, nullable = false)
    private int scoreTeam1;
    @Column(name = "score_team_2", updatable = false, nullable = false)
    private int scoreTeam2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayedMap playedMap = (PlayedMap) o;
        return matchId == playedMap.matchId && mapId == playedMap.mapId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, mapId);
    }

    @Override
    public String toString() {
        return "PlayedMap{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", mapId=" + mapId +
                ", scoreTeam1=" + scoreTeam1 +
                ", scoreTeam2=" + scoreTeam2 +
                '}';
    }
}
