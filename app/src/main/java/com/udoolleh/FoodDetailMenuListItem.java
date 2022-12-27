package com.udoolleh;

public class FoodDetailMenuListItem {
    String name;
    String photo;
    String price;
    String description;

    public FoodDetailMenuListItem(String name, String photo, String price, String description) {
        this.name = name;
        this.photo = photo;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
