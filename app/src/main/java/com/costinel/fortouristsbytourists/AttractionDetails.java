package com.costinel.fortouristsbytourists;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AttractionDetails extends AppCompatActivity {

    // creating the impostors for the attraction details activity layout
    private TextView name, location, description, price;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attraction_details);

        // linking the impostors textviews and imageviews to the ones on the activity layout;
        name = findViewById(R.id.attraction_details_name);
        location = findViewById(R.id.attraction_details_location);
        description = findViewById(R.id.attraction_details_description);
        price = findViewById(R.id.attraction_details_price);
        image = findViewById(R.id.attraction_details_image);

        // getting the intent values parsed from "ImageAdapter" and putting them in variables;
        String iName = getIntent().getStringExtra("attraction_name");
        String iLocation = getIntent().getStringExtra("attraction_location");
        String iDescription = getIntent().getStringExtra("attraction_description");
        String iPrice = getIntent().getStringExtra("attraction_price");
        String iImage = getIntent().getStringExtra("attraction_image");


        // setting the values from the variables to the impostors;
        name.setText(iName);
        location.setText(iLocation);
        description.setText(iDescription);
        price.setText(iPrice);




        //TO IMPLEMENT A RECYCLERVIEW
        Picasso.get().load(iImage).into(image);
    }
}
