package com.program.shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PredictionInfo {
    int type;
    long matchId;
    String team1;
    String team2;
    double points1;
    double points2;
    boolean firstWin;

    Map<String, Double> mapsPoints1 = new HashMap<>();
    Map<String, Double> mapsPoints2 = new HashMap<>();

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

    public Map<String, Double> getMapsPoints1() {
        return mapsPoints1;
    }

    public void setMapsPoints1(Map<String, Double> mapsPoints1) {
        this.mapsPoints1 = mapsPoints1;
    }

    public Map<String, Double> getMapsPoints2() {
        return mapsPoints2;
    }

    public void setMapsPoints2(Map<String, Double> mapsPoints2) {
        this.mapsPoints2 = mapsPoints2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type + " -- " + matchId + ": " +  team1 + " vs " + team2
                    + ", winner: " + (firstWin ? team1 : team2)
                    + ", points: " + points1 + " vs " + points2);

        sb.append(", mapsPoints1: {");
        sb.append(convertMapsPointsToString(true));
        sb.append("}");

        sb.append(", mapsPoints2: {");
        sb.append(convertMapsPointsToString(false));
        sb.append("}");

        return sb.toString();
    }

    private String convertMapsPointsToString(boolean isFirst) {
        StringBuilder s = new StringBuilder();

        Map<String, Double> mapsPoints = isFirst ? mapsPoints1 : mapsPoints2;

        String[] keys = mapsPoints.keySet().toArray(String[]::new);

        if (keys.length == 0) {
            return "null";

        }
        for (int i = 0; i < keys.length - 1; i++) {
            String name = keys[i];

            s.append(name);
            s.append("=");
            s.append(mapsPoints.get(name));
            s.append(", ");
        }
        String name = keys[keys.length - 1];
        s.append(name);
        s.append("=");
        s.append(mapsPoints.get(name));

        return s.toString();
    }
}
