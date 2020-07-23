package com.example.wingsproject.VenueImage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageResults {

    @SerializedName("items")
    private List<ImageItems> items;

    public List<ImageItems> getItems() {
        return items;
    }

    public void setItems(List<ImageItems> items) {
        this.items = items;
    }

}
