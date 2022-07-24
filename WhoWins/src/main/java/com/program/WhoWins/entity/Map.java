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
@Table(name = "maps")
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "match_id", updatable = false, nullable = false)
    private long matchId;
    @Column(name = "map_type_id", updatable = false, nullable = false)
    private long mapTypeId;
    @Column(name = "score_team_1", updatable = false, nullable = false)
    private int scoreTeam1;
    @Column(name = "score_team_2", updatable = false, nullable = false)
    private int scoreTeam2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;
        return matchId == map.matchId && mapTypeId == map.mapTypeId;
    }

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", mapTypeId=" + mapTypeId +
                ", scoreTeam1=" + scoreTeam1 +
                ", scoreTeam2=" + scoreTeam2 +
                '}';
    }
}
