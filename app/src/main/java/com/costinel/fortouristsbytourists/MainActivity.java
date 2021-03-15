package com.costinel.fortouristsbytourists;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.costinel.fortouristsbytourists.Model.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    // creating the impostor for the login button in the activity layout;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linking the impostor to the button in the activity layout;
        login = findViewById(R.id.bt_login_main);

        // creating an onClickListener for the login button with an Intent to send the user to the
        // login activity layout;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(MainActivity.this, Login.class);
                startActivity(login);
            }
        });

        // linking the recyclerView field to the recyclerView from the main activity;
        mRecyclerView = findViewById(R.id.recycle_view_main);

        // setting the recyclerView as fixed;
        mRecyclerView.setHasFixedSize(true);

        // setting a new Layout manager for this context;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attractions");

        // creating an addValueEventListener to the database reference to extract the data from
        // the mUploads ArrayList using the dataSnapshot;
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                // creating a new image adapter for this context;
                mAdapter = new ImageAdapter(MainActivity.this, mUploads);

                // setting the adapter for the recyclerView;
                mRecyclerView.setAdapter(mAdapter);
            }

            // this method will execute if there is an error while extracting the data;
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}