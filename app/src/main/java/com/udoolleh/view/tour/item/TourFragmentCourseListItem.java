package com.udoolleh.view.tour.item;

public class TourFragmentCourseListItem {
    int id;
    String courseName;
    String course;
    String detail_type_title;
    String detail_type_title_context;
    String detail_type_photo;
    String detail_type_photo_context;
    String detail_type_text;
    String detail_type_text_context;
    double gps_latitude;
    double gps_longitude;

    public TourFragmentCourseListItem(int id, String courseName, String course, String detail_type_title, String detail_type_title_context, String detail_type_photo, String detail_type_photo_context, String detail_type_text, String detail_type_text_context, double gps_latitude, double gps_longitude) {
        this.id = id;
        this.courseName = courseName;
        this.course = course;
        this.detail_type_title = detail_type_title;
        this.detail_type_title_context = detail_type_title_context;
        this.detail_type_photo = detail_type_photo;
        this.detail_type_photo_context = detail_type_photo_context;
        this.detail_type_text = detail_type_text;
        this.detail_type_text_context = detail_type_text_context;
        this.gps_latitude = gps_latitude;
        this.gps_longitude = gps_longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDetail_type_title() {
        return detail_type_title;
    }

    public void setDetail_type_title(String detail_type_title) {
        this.detail_type_title = detail_type_title;
    }

    public String getDetail_type_title_context() {
        return detail_type_title_context;
    }

    public void setDetail_type_title_context(String detail_type_title_context) {
        this.detail_type_title_context = detail_type_title_context;
    }

    public String getDetail_type_photo() {
        return detail_type_photo;
    }

    public void setDetail_type_photo(String detail_type_photo) {
        this.detail_type_photo = detail_type_photo;
    }

    public String getDetail_type_photo_context() {
        return detail_type_photo_context;
    }

    public void setDetail_type_photo_context(String detail_type_photo_context) {
        this.detail_type_photo_context = detail_type_photo_context;
    }

    public String getDetail_type_text() {
        return detail_type_text;
    }

    public void setDetail_type_text(String detail_type_text) {
        this.detail_type_text = detail_type_text;
    }

    public String getDetail_type_text_context() {
        return detail_type_text_context;
    }

    public void setDetail_type_text_context(String detail_type_text_context) {
        this.detail_type_text_context = detail_type_text_context;
    }

    public double getGps_latitude() {
        return gps_latitude;
    }

    public void setGps_latitude(double gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    public double getGps_longitude() {
        return gps_longitude;
    }

    public void setGps_longitude(double gps_longitude) {
        this.gps_longitude = gps_longitude;
    }
}
