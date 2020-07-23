package com.example.wingsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class VenueDescriptionActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_description);

        Context context = getApplicationContext();

        /** Getting all the info about the venue description as an IntentExtra  */
        String[] venueDescription = getIntent().getStringArrayExtra("description");

        ImageView venueImage = (ImageView) findViewById(R.id.image);
        TextView venueTitle = (TextView) findViewById(R.id.title);
        TextView venueName = (TextView) findViewById(R.id.name);
        TextView venueCountry = (TextView) findViewById(R.id.country);
        TextView venueCity = (TextView) findViewById(R.id.city);
        TextView venueAddress = (TextView) findViewById(R.id.address);
        TextView venuePostal = (TextView) findViewById(R.id.postal_code);

        if(venueDescription[0].equals("null")){
            Toast toast = Toast.makeText(context, "No photos found for "+venueDescription[3], Toast.LENGTH_SHORT);
            toast.show();
        }

        Picasso.get().load(venueDescription[0])
                .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                .resize(250, 250)
                .into(venueImage);

        venueTitle.setText(venueDescription[3]);
        venueAddress.setText(venueDescription[2]);
        venueName.setText(venueDescription[1]);
        venueCountry.setText(venueDescription[4]);
        venueCity.setText(venueDescription[5]);
        venuePostal.setText(venueDescription[6]);
    }
}
