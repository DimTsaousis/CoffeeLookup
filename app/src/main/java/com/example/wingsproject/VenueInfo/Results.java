package com.example.wingsproject.VenueInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {

    @SerializedName("venues")
    private List<Venues> venues;

    public List<Venues> getVenues() {
        return venues;
    }

    public void setVenues(List<Venues> venues) {
        this.venues = venues;
    }
}
