package com.udoolleh.view.map.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MapFragmentTimetableResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private timetableList list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public timetableList getList() {
        return list;
    }

    public void setList(timetableList list) {
        this.list = list;
    }

    public class timetableList {

        @SerializedName("destination")
        private String destination;

        @SerializedName("timetableDtos")
        private java.util.List<timetableDtos> timetableDtos = new ArrayList<>();

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public java.util.List<timetableDtos> getTimetableDtos() {
            return timetableDtos;
        }

        public void setTimetableDtos(java.util.List<timetableDtos> timetableDtos) {
            this.timetableDtos = timetableDtos;
        }

        public class timetableDtos {

            @SerializedName("id")
            private Integer id;

            @SerializedName("period")
            private String period;

            @SerializedName("operatingTime")
            private String operatingTime;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
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
    }
}
