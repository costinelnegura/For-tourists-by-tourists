package com.costinel.fortouristsbytourists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.costinel.fortouristsbytourists.Model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetails extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;

    // creating the impostors
    private Button cancel, update;
    private TextView userFirstName, userLastName, userEmail;
    private ImageView userAvatar;


    private Uri avatarImageURI;

    private Users user;

    private String key;


    private StorageTask mAvatarStorageTask;


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

//        String userKey = getIntent().getStringExtra("user_key");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("user");
        mStorageRef = FirebaseStorage.getInstance().getReference("images/avatar");


//        List<Users> user;

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShop : snapshot.getChildren()) {
                    user = postSnapShop.getValue(Users.class);
                    key = postSnapShop.getKey();

                    System.out.println(key);

                    //to resolve the exception that is thrown here
                    Picasso.get().load(user.getmAvatarUrl()).into(userAvatar);
                    String mUserFirstName = user.getFirstName();
                    String mUserLastName = user.getLastName();
                    String mUserEmail = user.getEmail();
                    String mUserAvatar = user.getmAvatarUrl();

                    userFirstName.setText(mUserFirstName);
                    userLastName.setText(mUserLastName);
                    userEmail.setText(mUserEmail);
                    Picasso.get().load(mUserAvatar).into(userAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDetails.this, MainActivity_logged_in.class);
                startActivity(i);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(avatarImageURI));

                mAvatarStorageTask = fileReference.putFile(avatarImageURI).
                        addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();

                        //this updates the user info using the user key
                        user = new Users(userFirstName.getText().toString(), userFirstName.getText().toString(),
                                user.getPassword(), userEmail.getText().toString(), downloadUrl.toString());

                        mDatabaseRef.child(key).setValue(user);
                    }
                });
            }
        });
//        Intent i = new Intent(UserDetails.this, MainActivity_logged_in.class);
//        startActivity(i);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            avatarImageURI = data.getData();

            Picasso.get().load(avatarImageURI).into(userAvatar);
        }
    }


    // this method returns the extension of the image file that was selected;
    // ex: an image.jpg, the method wil return "jpg";
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}