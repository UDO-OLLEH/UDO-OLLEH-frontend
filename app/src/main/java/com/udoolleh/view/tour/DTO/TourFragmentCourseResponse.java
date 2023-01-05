package com.udoolleh.view.tour.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourFragmentCourseResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("message")
    private String message;

    @SerializedName("list")
    private List<CourseList> list = null;

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public List<CourseList> getCourseList() {
        return list;
    }

    public class CourseList {
        @SerializedName("id")
        private Integer id;

        @SerializedName("courseName")
        private String courseName;

        @SerializedName("course")
        private String course;

        @SerializedName("detail")
        private List<Detail> detail = null;

        @SerializedName("gps")
        private List<CourseGps> gps = null;

        public Integer getId() {
            return id;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getCourse() {
            return course;
        }

        public List<Detail> getDetail() {
            return detail;
        }

        public List<CourseGps> getGps() {
            return gps;
        }

        public class Detail {
            @SerializedName("type")
            private String type;

            @SerializedName("context")
            private String context;

            public String getType() {
                return type;
            }

            public String getContext() {
                return context;
            }
        }

        public class CourseGps {
            @SerializedName("latitude")
            private Double latitude;

            @SerializedName("longitude")
            private Double longitude;

            public Double getLatitude() {
                return latitude;
            }

            public Double getLongitude() {
                return longitude;
            }
        }
    }
}
