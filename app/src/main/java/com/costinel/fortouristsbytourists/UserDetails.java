package com.costinel.fortouristsbytourists;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.costinel.fortouristsbytourists.Model.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class UserDetails extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference mDatabaseRef;

    private static final String USER = "user";

    // creating the impostors
    private Button cancel, update;
    private TextView userFirstName, userLastName, userEmail;
    private ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // creating an instance of FirebaseDatabase with the reference to the USER node;
//        database = FirebaseDatabase.getInstance().getReference("user"+);
//        mDatabaseRef = database.getReference(USER);

        // linking the impostors with the buttons, text views and image views in the activity layout
        cancel = findViewById(R.id.cancel_update_user_details_bt);
        update = findViewById(R.id.update_user_details_bt);
        userFirstName = findViewById(R.id.user_first_name_txt);
        userLastName = findViewById(R.id.user_last_name_txt);
        userEmail = findViewById(R.id.user_email_txt);
        userAvatar = findViewById(R.id.user_avatar_img);

        String userKey = getIntent().getStringExtra("user_key");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user/" + userKey);
        Users user = new Users();
        mDatabaseRef.child(userKey).setValue(user);


        //Receiving the user information parsed from the main_activity_logged_in activity
        String mUserAvatar = user.getmAvatarUrl();
        String mUserFirstName = user.getFirstName();
        String mUserLastName = user.getLastName();
        String mUserEmail = user.getEmail();

        Picasso.get().load(mUserAvatar).into(userAvatar);

        //will display the actual information as a hist, so the user can tap the text box and
        // type new information.
        userFirstName.setHint(mUserFirstName);
        userLastName.setHint(mUserLastName);
        userEmail.setHint(mUserEmail);
    }
}