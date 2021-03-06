package com.costinel.fortouristsbytourists;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.costinel.fortouristsbytourists.Model.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_logged_in extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    // creating the impostors for the buttons from the activity layout;
    Button logout, create_attraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged_in);

        // linking the impostors to the activity layout buttons;
        logout = findViewById(R.id.bt_login_main);
        create_attraction = findViewById(R.id.bt_create_attraction);

        // creating an OnClickListener for the logout button to log out the user and
        // sending him to the main activity where he doesn't have privileges to add attractions;
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(MainActivity_logged_in.this,
                        MainActivity.class);
                startActivity(logout);
            }
        });

        // creating an onClickListener for the create attraction button to allow the user to
        // add attractions;
        // the user will be sent to the upload_images activity layout ;
        create_attraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image_upload = new Intent(MainActivity_logged_in.this,
                        Upload_Images.class);
                startActivity(image_upload);
            }
        });

        // linking the recyclerView field to the recyclerview from the main activity layout where
        // the user is logged in;
        mRecyclerView = findViewById(R.id.recyclerView_logged_in);

        // declaring the recyclerView as fixed;
        mRecyclerView.setHasFixedSize(true);

        // setting the layout for the recyclerView for this context;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attractions");

        // creating an addValueEventListener to extract the data from the Firebase attraction node;
        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            // this method will extract the data using dataSnapshot from the attraction
            // children nodes;
            // will create a new ArrayList of upload objects;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                // creating a new image adapter for this context;
                mAdapter = new ImageAdapter(MainActivity_logged_in.this, mUploads);

                // setting the image adapter to the recyclerView;
                mRecyclerView.setAdapter(mAdapter);
            }

            // this method will be executed if there is an error in retrieving the data
            // from Firebase;
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity_logged_in.this, databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}