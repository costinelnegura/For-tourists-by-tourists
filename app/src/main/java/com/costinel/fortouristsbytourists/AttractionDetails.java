package com.costinel.fortouristsbytourists;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.costinel.fortouristsbytourists.AttractionLoader.AttractionDetails_ImageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttractionDetails extends AppCompatActivity {

    // creating the impostors for the attraction details activity layout
    private TextView name, location, description, price;
    private ImageView image;
    private RecyclerView recyclerView;

    private AttractionDetails_ImageAdapter imageAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attraction_details);

        recyclerView = findViewById(R.id.img_attraction_images_recyclerview);

        mAuth = FirebaseAuth.getInstance();

        // linking the impostors textviews and imageviews to the ones on the activity layout;
        name = findViewById(R.id.attraction_details_name);
        location = findViewById(R.id.attraction_details_location);
        description = findViewById(R.id.attraction_details_description);
        price = findViewById(R.id.attraction_details_price);
        recyclerView = findViewById(R.id.img_attraction_images_recyclerview);


        // getting the intent values parsed from "ImageAdapter" and putting them in variables;
        String iName = getIntent().getStringExtra("name");
        String iLocation = getIntent().getStringExtra("location");
        String iDescription = getIntent().getStringExtra("description");
        String iPrice = getIntent().getStringExtra("price");
        List<Object> imgUri = (List<Object>) getIntent().getSerializableExtra("images");

        // setting the values from the variables to the impostors;
        name.setText(iName);
        location.setText(iLocation);
        description.setText(iDescription);
        price.setText(iPrice);

        recyclerView.setLayoutManager(new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false));

        imageAdapter = new AttractionDetails_ImageAdapter(AttractionDetails.this, imgUri);


        recyclerView.setAdapter(imageAdapter);
    }
}
