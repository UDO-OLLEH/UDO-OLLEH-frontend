package com.udoolleh.view.map.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapFragmentShipfareResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private List<shipfareList> list;

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

    public List<shipfareList> getList() {
        return list;
    }

    public void setList(List<shipfareList> list) {
        this.list = list;
    }

    public class shipfareList {
        @SerializedName("harborName")
        @Expose
        private String harborName;
        @SerializedName("shipFareDtos")
        @Expose
        private java.util.List<shipfareDto> shipFareDtos = null;

        public String getHarborName() {
            return harborName;
        }

        public void setHarborName(String harborName) {
            this.harborName = harborName;
        }

        public java.util.List<shipfareDto> getShipFareDtos() {
            return shipFareDtos;
        }

        public void setShipFareDtos(java.util.List<shipfareDto> shipFareDtos) {
            this.shipFareDtos = shipFareDtos;
        }
    }

    public class shipfareDto {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("ageGroup")
        @Expose
        private String ageGroup;
        @SerializedName("roundTrip")
        @Expose
        private Integer roundTrip;
        @SerializedName("enterIsland")
        @Expose
        private Integer enterIsland;
        @SerializedName("leaveIsland")
        @Expose
        private Integer leaveIsland;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAgeGroup() {
            return ageGroup;
        }

        public void setAgeGroup(String ageGroup) {
            this.ageGroup = ageGroup;
        }

        public Integer getRoundTrip() {
            return roundTrip;
        }

        public void setRoundTrip(Integer roundTrip) {
            this.roundTrip = roundTrip;
        }

        public Integer getEnterIsland() {
            return enterIsland;
        }

        public void setEnterIsland(Integer enterIsland) {
            this.enterIsland = enterIsland;
        }

        public Integer getLeaveIsland() {
            return leaveIsland;
        }

        public void setLeaveIsland(Integer leaveIsland) {
            this.leaveIsland = leaveIsland;
        }
    }
}
