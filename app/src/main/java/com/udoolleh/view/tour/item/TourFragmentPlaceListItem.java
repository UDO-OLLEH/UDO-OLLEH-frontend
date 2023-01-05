package com.udoolleh.view.tour.item;

public class TourFragmentPlaceListItem {
    int id;
    String placeName;
    String intro;
    String photo;

    public TourFragmentPlaceListItem(int id, String placeName, String intro, String photo) {
        this.id = id;
        this.placeName = placeName;
        this.intro = intro;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
