package com.example.wingsproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.wingsproject.VenueImage.ImageItems;
import com.example.wingsproject.VenueImage.ImageResults;
import com.example.wingsproject.VenueImage.JsonFileImage;
import com.example.wingsproject.VenueInfo.JsonPlaceApi;
import com.example.wingsproject.VenueInfo.Categories;
import com.example.wingsproject.VenueInfo.JsonFile;
import com.example.wingsproject.VenueImage.JsonImageApi;
import com.example.wingsproject.VenueInfo.Results;
import com.example.wingsproject.VenueInfo.Venues;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String client_id = "DJHFQQGD0VXCMRKBQUPDDW1HPBKFVFRTFCZV2TELTL1S14JA";
    private String client_secret = "VYM0IMY50LDYCPT5ZNLWICJTW0SVWFHR3FVGNNX50ZMYG1PL";
    private String query = "Coffee";
    private String v = "20200721";
    private String radius = "1000";
    private String url_of_image = null;
    private String venuePhotos = "1";

    private LatLng myLocation;
    private GoogleMap mMap;

    // A default location (Athens, Greece) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(37.9838, 23.7275);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new CustomMarkerInfoAdapter(MapsActivity.this));

        getDeviceLocation();

        /** If the camera gets moved, this listener is triggerd to show new markers on the map*/
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                Double lat = mMap.getCameraPosition().target.latitude;
                Double lng = mMap.getCameraPosition().target.longitude;

                getLocalPlaces(lat,lng);
            }
        });

    }
    /**
     * Gets the permission to use location services.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                myLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                getLocationAddress();
                            }
                        } else {
                            Log.d("ERROR", "Current location is null. Using defaults.");
                            Log.e("ERROR", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Gets the address of the device  based on given LatLng.
     */
    private void getLocationAddress() {
        Geocoder geoCoder = new Geocoder(getApplicationContext());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address bestMatch = (matches.isEmpty() ? null : matches.get(0));

        TextView myAddress = (TextView)findViewById(R.id.address);
        myAddress.setText("Your Address is: "+ bestMatch.getAddressLine(0));
    }

    /**
     * Marks location and gathers information about our given subject based on Lat and Lng parameters.
     */
    private void getLocalPlaces(final Double latitude, final Double longitude) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/v2/venues/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceApi myFoursquareService = retrofit.create(JsonPlaceApi.class);

        /** This is the actual call on Foursquare for potential results(Json) */
        Call<JsonFile> fileCall = myFoursquareService.listRepos(client_id, client_secret, query, v, radius,latitude+","+longitude);

        fileCall.enqueue(new Callback<JsonFile>() {
            @Override
            public void onResponse(Call<JsonFile> call, Response<JsonFile> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR", String.valueOf(response.code()));
                    return;
                }

                JsonFile jsonFile = response.body();
                Results results = jsonFile.getResults();

                for (Venues venue : results.getVenues()) {
                    for(Categories categories : venue.getCategories()) {

                        /** Checking if the address info is null */
                        String addressCheck = "-";
                        if(venue.getLocation().getAddress()!=null){
                            addressCheck = venue.getLocation().getAddress();
                        }
                        /** Checking if the postalCode info is null */
                        String postalCheck = "-";
                        if(venue.getLocation().getPostalCode()!=null){
                            postalCheck = venue.getLocation().getPostalCode();
                        }
                        /** Checking if the postalCode info is null */
                        String cityCheck = "-";
                        if(venue.getLocation().getCity()!=null){
                            cityCheck = venue.getLocation().getCity();
                        }

                        String imageURL = imageFinder(venue.getId());

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(venue.getLocation().getLat()),(Double.parseDouble(venue.getLocation().getLng()))))
                                .snippet(imageURL+"*"+categories.getName()+"*"+addressCheck+"*"+venue.getName()+"*"+venue.getLocation().getCountry()+"*"+cityCheck+"*"+postalCheck)
                                .title(venue.getName()))
                                .setIcon(bitmapDescriptor(getApplicationContext(),R.drawable.ic_local_cafe_black_24dp));

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                String[] venueDescription = new String[7];

                                /** Splitting the snippet into fields */
                                String[] words = marker.getSnippet().split("\\*");
                                venueDescription[0] = words[0];
                                venueDescription[1] = words[1];
                                venueDescription[2] = words[2];
                                venueDescription[3] = words[3];
                                venueDescription[4] = words[4];
                                venueDescription[5] = words[5];
                                venueDescription[6] = words[6];

                                Intent intent = new Intent(MapsActivity.this,VenueDescriptionActivity.class);
                                intent.putExtra("description",venueDescription);
                                startActivity(intent);
                            }
                        });
                    }

                }


            }
            @Override
            public void onFailure(Call<JsonFile> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
    
    private String imageFinder(String venue_id){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/v2/venues/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonImageApi myFoursquareService = retrofit.create(JsonImageApi.class);

        /** This is the actual call on Foursquare for potential results(Json) */
        Call<JsonFileImage> fileCall = myFoursquareService.listRepos(venue_id, client_id, client_secret, v, venuePhotos);

        fileCall.enqueue(new Callback<JsonFileImage>() {
            @Override
            public void onResponse(Call<JsonFileImage> call, Response<JsonFileImage> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR_IMAGE", String.valueOf(response.code()));
                    return;
                }

                JsonFileImage jsonFileImage = response.body();

                if(jsonFileImage.getImageResults().getItems()!=null){

                    ImageResults imageResults = jsonFileImage.getImageResults();

                    ImageItems[] imageItemsArray = new ImageItems[0];;

                    for (ImageItems imageItems : imageResults.getItems()) {
                        /** I always get one photo, thats why I only save in position 0*/
                        imageItemsArray[0]= imageItems;
                    }

                    url_of_image = imageItemsArray[0].getPrefix() + "original" + imageItemsArray[0].getSuffix();
                }

            }
            @Override
            public void onFailure(Call<JsonFileImage> call, Throwable t) {
                Log.d("ERROR_IMAGE", t.getMessage());
            }
        });

        return url_of_image;
    }

    /**
     * BitmapDescriptor for setting up my GoogleMaps marker icon
     */
    private BitmapDescriptor bitmapDescriptor (Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);

        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
