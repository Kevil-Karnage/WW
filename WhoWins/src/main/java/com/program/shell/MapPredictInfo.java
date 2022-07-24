package com.program.shell;

public class MapPredictInfo {
    double points1;
    double points2;
    String mapName;
    boolean firstWin;

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

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public boolean isFirstWin() {
        return firstWin;
    }

    public void setFirstWin(boolean firstWin) {
        this.firstWin = firstWin;
    }

    @Override
    public String toString() {
        return "mapName='" + mapName + '\'' +
                "points1=" + points1 +
                ", points2=" + points2 +
                ", winner=" + firstWin;
    }
}
