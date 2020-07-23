package com.example.wingsproject.VenueImage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonImageApi {
//    https://api.foursquare.com/v2/venues/4d66948f6939cbff14ea8856/photos?venuePhotos=1&client_id=ORDOUJMOJDBOLY1ZU0WMUADMQEKNHUI4AME4GGJGLEZQWTFO&client_secret=WT15QOX1XQIRZQOUB3JNIENIYSTHFYA1HZFLNY201RRCDJR5&v=20200721
    @GET("{venue_id}/photos")
    Call<JsonFileImage> listRepos(@Path ("venue_id") String venue_id, @Query("client_id") String client_id, @Query("client_secret") String client_secret, @Query("v") String v, @Query("venuePhotos") String venuePhotos);
}
