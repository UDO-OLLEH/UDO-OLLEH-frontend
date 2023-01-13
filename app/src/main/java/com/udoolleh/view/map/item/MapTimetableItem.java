package com.udoolleh.view.map.item;

public class MapTimetableItem {
    String period;
    String operatingTime;

    public MapTimetableItem(String period, String operatingTime) {
        this.period = period;
        this.operatingTime = operatingTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }
}
