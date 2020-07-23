package com.example.wingsproject.VenueInfo;

import com.example.wingsproject.VenueInfo.JsonFile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceApi {
//    https://api.foursquare.com/v2/venues/search?client_id=ORDOUJMOJDBOLY1ZU0WMUADMQEKNHUI4AME4GGJGLEZQWTFO&client_secret=WT15QOX1XQIRZQOUB3JNIENIYSTHFYA1HZFLNY201RRCDJR5&query=gas&v=20200721&ll=40.7,-74
    @GET("search")
    Call<JsonFile> listRepos(@Query("client_id") String client_id, @Query("client_secret") String client_secret, @Query("query") String query, @Query("v") String v, @Query("radius") String radius, @Query("ll") String ll);
}
