package com.costinel.fortouristsbytourists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.costinel.fortouristsbytourists.AttractionLoader.ImageAdapter;
import com.costinel.fortouristsbytourists.Model.Attraction;
import com.costinel.fortouristsbytourists.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "User Data";

    private RecyclerView mRecyclerView;

    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    private  Map<Attraction, List<Uri>> attractions = new HashMap<>();

    private Users user;
    private String userUid;

    Boolean userLoginCheck = false;

    // creating the impostors for the buttons from the activity layout;
    Button logout_login, create_attraction;
    ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linking the impostors to the activity layout buttons;
        logout_login = findViewById(R.id.bt_login_logout_main);
        create_attraction = findViewById(R.id.bt_create_attraction);
        userAvatar = findViewById(R.id.logged_in_user_avatar);

        mAuth = FirebaseAuth.getInstance();

        Boolean temp = getIntent().getBooleanExtra("logged", false);
        userLoginCheck = temp;

        if(!userLoginCheck){
            create_attraction.setVisibility(View.INVISIBLE);
            userAvatar.setVisibility(View.INVISIBLE);
            logout_login.setText(R.string.login);
        }else{
            create_attraction.setVisibility(View.VISIBLE);
            userAvatar.setVisibility(View.VISIBLE);
            logout_login.setText(R.string.logout);
        }

        logout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userLoginCheck) {
                    Intent login = new Intent(MainActivity.this, Login.class);
                    startActivity(login);
                }else{
                    mAuth.signOut();
                    userLoginCheck = false;
                    logout_login.setText(R.string.login);
                    create_attraction.setVisibility(View.INVISIBLE);
                    userAvatar.setVisibility(View.INVISIBLE);
                }

            }
        });


        userUid = getIntent().getStringExtra("UID");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("user/" + userUid);

        if(userLoginCheck) {
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(Users.class);
                    Picasso.get().load(user.getmAvatarUrl()).into(userAvatar);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Log.d(TAG, "error retrieving data");
                }
            });
        }

        // creating an onClickListener for the create attraction button to allow the user to
        // add attractions;
        // the user will be sent to the upload_images activity layout ;
        create_attraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image_upload = new Intent(MainActivity.this,
                        Upload_Images.class);
                startActivity(image_upload);
            }
        });

        // creating an onClickListener for the userAvatar ImageView to allow the user to
        // view details about his account;
        // the user will be sent to the activity_user_details activity layout;
        // i.putExtra will have to parse over to the other activity all the information about the user.
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserDetails.class);
                i.putExtra("UID", userUid);
                startActivity(i);
            }
        });


        List<String> imagesKey = new ArrayList<>();

        // linking the recyclerView field to the recyclerview from the main activity layout where
        // the user is logged in;
        mRecyclerView = findViewById(R.id.recyclerView_logged_in);

        // declaring the recyclerView as fixed;
        mRecyclerView.setHasFixedSize(true);

        // setting the layout for the recyclerView for this context;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attractions");


        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/attractions");


        // creating an addValueEventListener to extract the data from the Firebase attraction node;
        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            // this method will extract the data using dataSnapshot from the attraction
            // children nodes;
            // will create a new ArrayList of upload objects;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    Attraction attraction = postSnapshot.getValue(Attraction.class);

                    String attractionID = postSnapshot.getKey();

                    Iterator<DataSnapshot> iterator = dataSnapshot.child(attractionID).child("images").getChildren().iterator();
                    while(iterator.hasNext()){
                        imagesKey.add(iterator.next().getKey());
                    }

                    for(int i=0; i<imagesKey.size(); i++){
                        storageReference.child("/1616778063655img").getDownloadUrl().addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        List<Uri> thumbnails = new ArrayList<>();
                                        thumbnails.add(uri);
                                        createRecyclerView(attraction, thumbnails);
                                    }
                                }
                        );

                    }
                }

            }

            // this method will be executed if there is an error in retrieving the data
            // from Firebase;
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRecyclerView(Attraction attraction, List<Uri> uri){

        attractions.put(attraction, uri);
        // creating a new image adapter for this context;
        mAdapter = new ImageAdapter(MainActivity.this, attractions);

        // setting the adapter for the recyclerView;
        mRecyclerView.setAdapter(mAdapter);
    }
}