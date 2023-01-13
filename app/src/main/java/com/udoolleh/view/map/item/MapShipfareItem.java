package com.udoolleh.view.map.item;

public class MapShipfareItem {
    String ageGroup;
    int roundTrip;
    int enterIsland;
    int leaveIsland;

    public MapShipfareItem(String ageGroup, int roundTrip, int enterIsland, int leaveIsland) {
        this.ageGroup = ageGroup;
        this.roundTrip = roundTrip;
        this.enterIsland = enterIsland;
        this.leaveIsland = leaveIsland;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public int getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(int roundTrip) {
        this.roundTrip = roundTrip;
    }

    public int getEnterIsland() {
        return enterIsland;
    }

    public void setEnterIsland(int enterIsland) {
        this.enterIsland = enterIsland;
    }

    public int getLeaveIsland() {
        return leaveIsland;
    }

    public void setLeaveIsland(int leaveIsland) {
        this.leaveIsland = leaveIsland;
    }
}
