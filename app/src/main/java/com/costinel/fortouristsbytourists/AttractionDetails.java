package com.costinel.fortouristsbytourists;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.costinel.fortouristsbytourists.AttractionLoader.AttractionDetails_ImageAdapter;
import com.costinel.fortouristsbytourists.Model.Attraction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AttractionDetails extends AppCompatActivity {

    // creating the impostors for the attraction details activity layout
    private TextView name, location, description, price;
    private ImageView image;
    private RecyclerView recyclerView;

    private AttractionDetails_ImageAdapter imageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attraction_details);

        recyclerView = findViewById(R.id.img_attraction_images_recyclerview);

        recyclerView.setHasFixedSize(true);


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
        List<Uri> imagesUrl = (List<Uri>) getIntent().getSerializableExtra("imagesUrl");



//        String attractionKey = getIntent().getStringExtra("attractionKey");


        // setting the values from the variables to the impostors;
        name.setText(iName);
        location.setText(iLocation);
        description.setText(iDescription);
        price.setText(iPrice);

        createRecyclerView(imagesUrl);

//        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("attractions").
//                child(attractionKey);
//
//        // creating an addValueEventListener to extract the data from the Firebase attraction node;
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//
//            // this method will extract the data using dataSnapshot from the attraction
//            // children nodes;
//            // will create a new ArrayList of upload objects;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
////                    imagesUrl.add(Uri.parse(postSnapshot.getValue().toString()));
//
//                    Iterator<DataSnapshot> iterator = dataSnapshot.child("images").getChildren().iterator();
//                    while(iterator.hasNext()){
//                        imagesUrl.add(Uri.parse(iterator.next().getValue().toString()));
//
//                    }
//                }
//
//                createRecyclerView(imagesUrl);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void createRecyclerView(List<Uri> imagesUrl){
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        imageAdapter = new AttractionDetails_ImageAdapter(AttractionDetails.this, imagesUrl);


        recyclerView.setAdapter(imageAdapter);
    }

//    public static String getPathFromInputStreamUri(Context context, Uri uri) {
//        InputStream inputStream = null;
//        String filePath = null;
//
//        if (uri.getAuthority() != null) {
//            try {
//                inputStream = context.getContentResolver().openInputStream(uri);
//                File photoFile = createTemporalFileFrom(inputStream);
//
//                filePath = photoFile.getPath();
//
//            } catch (FileNotFoundException e) {
//                System.out.println(e);
//            } catch (IOException e) {
//                System.out.println(e);
//            } finally {
//                try {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return filePath;
//    }
//    private static File createTemporalFileFrom(InputStream inputStream) throws IOException {
//        File targetFile = null;
//
//        if (inputStream != null) {
//            int read;
//            byte[] buffer = new byte[8 * 1024];
//
//            targetFile = createTemporalFile();
//            OutputStream outputStream = new FileOutputStream(targetFile);
//
//            while ((read = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, read);
//            }
//            outputStream.flush();
//
//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return targetFile;
//    }
//
//    private static File createTemporalFile() {
//        final File file = new File(Utils.getDefaultDirectory(), "tempPicture.jpg");
//        return file;
//    }

}
