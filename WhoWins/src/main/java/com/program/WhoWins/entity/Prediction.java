package com.program.WhoWins.entity;

import javax.persistence.*;

@Entity
@Table(name = "predictions")
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "match_id", updatable = false, nullable = false)
    private Long matchId;
    @Column(name = "points1", updatable = false, nullable = false)
    private double points1;
    @Column(name = "points2", updatable = false, nullable = false)
    private double points2;
    @Column(name = "ended", updatable = true, nullable = false)
    private boolean ended;
    @Column(name = "correct", nullable = false)
    private boolean correct;
    @Column(name = "winner_id", updatable = false, nullable = false)
    private Long winnerId;
    /*@Column(name = "chance", updatable = false, nullable = false)
    private int chance;
    */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public double getPoints1() {
        return points1;
    }

    public void setPoints1(double points1) {
        this.points1 = points1;
    }

    public double getPoints2() {
        return points2;
    }

    public void setPoints2(double points2) {
        this.points2 = points2;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }
/*
    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
*/
    @Override
    public String toString() {
        return "Prediction{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", points1=" + points1 +
                ", points2=" + points2 +
                ", ended=" + ended +
                ", correct=" + correct +
                ", winnerId=" + winnerId +
                //", chance=" + chance + "%" +
                '}';
    }
}
