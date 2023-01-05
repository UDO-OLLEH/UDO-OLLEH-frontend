package com.udoolleh.view.tour.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourFragmentPlaceResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private List<PlaceList> list = null;

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

    public List<PlaceList> getPlaceList() {
        return list;
    }

    public void setPlaceList(List<PlaceList> list) {
        this.list = list;
    }

    public class PlaceList {
        @SerializedName("id")
        private Integer id;

        @SerializedName("placeName")
        private String placeName;

        @SerializedName("intro")
        private String intro;

        @SerializedName("context")
        private String context;

        @SerializedName("photo")
        private String photo;

        @SerializedName("gps")
        private List<PlaceGps> gps = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public List<PlaceGps> getGps() {
            return gps;
        }

        public void setGps(List<PlaceGps> gps) {
            this.gps = gps;
        }

        public class PlaceGps {
            @SerializedName("latitude")
            private Double latitude;

            @SerializedName("longitude")
            private Double longitude;

            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }

        }

    }
}
