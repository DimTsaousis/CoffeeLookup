package com.example.wingsproject.VenueInfo;

import com.google.gson.annotations.SerializedName;

public class JsonFile {

    @SerializedName("response")
    private Results results;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }
}
