package com.example.wingsproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class CustomMarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomMarkerInfoAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
    }

    private void rendowWindowText(Marker marker, View view){



        String title = marker.getTitle();
        TextView markerTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            markerTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView markerAddress = (TextView) view.findViewById(R.id.address);
        TextView markerName = (TextView) view.findViewById(R.id.name);

        /** Splitting the snippet to three fields (url, name, address) */
        String[] words = snippet.split("\\*");

        ImageView markerImage = (ImageView) view.findViewById(R.id.image);

        Picasso.get().load(words[0])
                .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                .resize(70, 70)
                .into(markerImage);

        if(!words[1].equals("")){
            markerName.setText(words[1]);
        }

        if(!words[2].equals("")){
            markerAddress.setText(words[2]);
        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }
}
