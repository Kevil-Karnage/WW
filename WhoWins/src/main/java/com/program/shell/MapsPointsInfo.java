package com.program.shell;

public class MapsPointsInfo {
    double allPoints;
    int count;

    public double getAllPoints() {
        return allPoints;
    }

    public void setAllPoints(double allPoints) {
        this.allPoints = allPoints;
    }

    public void addPoints(double points) {
        allPoints += points;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    @Override
    public String toString() {
        return "" + allPoints/count;
    }
}
