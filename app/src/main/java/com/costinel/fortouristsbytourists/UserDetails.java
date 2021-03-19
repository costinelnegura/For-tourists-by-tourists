package com.costinel.fortouristsbytourists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.costinel.fortouristsbytourists.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserDetails extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;

    // creating the impostors
    private Button cancel, update;
    private TextView userFirstName, userLastName, userEmail;
    private ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // linking the impostors with the buttons, text views and image views in the activity layout
        cancel = findViewById(R.id.cancel_update_user_details_bt);
        update = findViewById(R.id.update_user_details_bt);
        userFirstName = findViewById(R.id.user_first_name_txt);
        userLastName = findViewById(R.id.user_last_name_txt);
        userEmail = findViewById(R.id.user_email_txt);
        userAvatar = findViewById(R.id.user_avatar_img);

        String userKey = getIntent().getStringExtra("user_key");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("user");

        List<Users> user;

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShop : snapshot.getChildren()){
                    Users user = postSnapShop.getValue(Users.class);
                    Picasso.get().load(user.getmAvatarUrl()).into(userAvatar);
                    String mUserFirstName = user.getFirstName();
                    String mUserLastName = user.getLastName();
                    String mUserEmail = user.getEmail();
                    String mUserAvatar = user.getmAvatarUrl();

                    userFirstName.setHint(mUserFirstName);
                    userLastName.setHint(mUserLastName);
                    userEmail.setHint(mUserEmail);
                    Picasso.get().load(mUserAvatar).into(userAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}