package com.example.wingsproject.VenueInfo;

import com.example.wingsproject.VenueInfo.Categories;
import com.example.wingsproject.VenueInfo.Contact;
import com.example.wingsproject.VenueInfo.Location;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Venues {

    private String id;

    private String name;

    private Location location;

    private Contact contact;

    @SerializedName("categories")
    private List<Categories> categories;

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
