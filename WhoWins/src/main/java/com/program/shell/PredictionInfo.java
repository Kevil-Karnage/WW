package com.program.shell;

public class PredictionInfo {
    long matchId;
    String team1;
    String team2;
    double points1;
    double points2;
    boolean firstWin;
    int predictPoints;

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public boolean isFirstWin() {
        return firstWin;
    }

    public void setFirstWin(boolean firstWin) {
        this.firstWin = firstWin;
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

    public int getPredictPoints() {
        return predictPoints;
    }

    public void setPredictPoints(int predictPoints) {
        this.predictPoints = predictPoints;
    }

    public void incrementPredictPoints() {
        predictPoints++;
    }

    @Override
    public String toString() {
            return predictPoints + ": " +  team1 + " vs " + team2
                    + ", winner: " + (firstWin ? team1 : team2)
                    + ", points: " + points1 + " vs " + points2;
    }
}
