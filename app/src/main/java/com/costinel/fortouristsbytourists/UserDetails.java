package com.costinel.fortouristsbytourists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.costinel.fortouristsbytourists.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class UserDetails extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "User Details";

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    // creating the impostors
    private Button cancel, update, resetPassword;
    private TextView userFirstName, userLastName, userEmail, confirmationPassword;
    private ImageView userAvatar;

    private Uri avatarImageURI;

    private Users user;

    private String mUserAvatar;
    private String tempPassword;

    private StorageTask mAvatarStorageTask;


    private String[] tempAvatar = new String[1];


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
        confirmationPassword = findViewById(R.id.txt_confirmation_password);
        resetPassword = findViewById(R.id.bt_reset_password);
        userAvatar = findViewById(R.id.user_avatar_img);

        // this will receive the user id
        String userUid = getIntent().getStringExtra("UID");

        //creating instances of both database and storage pointing to the correct nodes
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("user/" + userUid);
        mStorageRef = FirebaseStorage.getInstance().getReference("images/avatar");

        //this is an event listener that returned the data from the node where mData ref is pointing
        //in this case it;s pointing to the logged in user node by using his user identifier.
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(Users.class);

                String mUserFirstName = user.getFirstName();
                String mUserLastName = user.getLastName();
                String mUserEmail = user.getEmail();
                mUserAvatar = user.getmAvatarUrl();


                userFirstName.setText(mUserFirstName);
                userLastName.setText(mUserLastName);
                userEmail.setText(mUserEmail);
                Picasso.get().load(user.getmAvatarUrl()).into(userAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "error retrieving data");
            }
        });

        //this opens the image picker once the avatar is taped
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDetails.this, MainActivity_logged_in.class);
                i.putExtra("UID", userUid);
                startActivity(i);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {

            String url = null;
            @Override
            public void onClick(View v) {
                if (avatarImageURI == null) {
                    tempPassword = confirmationPassword.getText().toString();
                    if(tempPassword.equals("")){
                        Toast.makeText(getApplicationContext(), "Type password to confirm!", Toast.LENGTH_LONG).show();
                    }else{
                        tempPassword = confirmationPassword.getText().toString();
                        updateUser(user.getmAvatarUrl());
                    }

                } else {
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(avatarImageURI));

                    mAvatarStorageTask = fileReference.putFile(avatarImageURI).
                            addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    Uri downloadUrl = urlTask.getResult();
                                    tempPassword = confirmationPassword.getText().toString();
                                    if(tempPassword.equals("")){
                                        Toast.makeText(getApplicationContext(), "Type password to confirm!", Toast.LENGTH_LONG).show();

                                    }else{
                                        tempPassword = confirmationPassword.getText().toString();
                                        updateUser(downloadUrl.toString());
                                        Intent i = new Intent(UserDetails.this, MainActivity_logged_in.class);
                                        i.putExtra("UID", userUid);
                                        startActivity(i);
                                    }

                                }
                            });
                }
            }
        });
    }

    private void updateUser(String url){



        //Updating the user email
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), tempPassword);

        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "User re-authenticated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "User not re-authenticated");
            }
        });

        firebaseUser.updateEmail(userEmail.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email updated");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error updating user email!");
            }
        });


        mDatabaseRef.child("email").setValue(userEmail.getText().toString());
        mDatabaseRef.child("firstName").setValue(userFirstName.getText().toString());
        mDatabaseRef.child("lastName").setValue((userLastName.getText().toString()));
        mDatabaseRef.child("mAvatarUrl").setValue(url);


        //this section deleted the old avatar file from Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(mUserAvatar);
        mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: deleted old avatar");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: did not delete the old avatar");
            }
        });
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