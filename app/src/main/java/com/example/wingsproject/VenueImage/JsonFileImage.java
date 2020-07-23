package com.example.wingsproject.VenueImage;

import com.google.gson.annotations.SerializedName;

public class JsonFileImage {

    @SerializedName("response")
    private ImageResults imageResults;

    public ImageResults getImageResults() {
        return imageResults;
    }

    public void setImageResults(ImageResults imageResults) {
        this.imageResults = imageResults;
    }
}
