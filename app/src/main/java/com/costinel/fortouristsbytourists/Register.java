package com.costinel.fortouristsbytourists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.costinel.fortouristsbytourists.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class Register extends AppCompatActivity {

    // creating a constant to use it to identify the image request;
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseDatabase database;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String USER = "user";
    private static final String TAG = "Register";

    // creating a global user object to use it in multiple methods;
    private Users user;

    // creating an URI variable to hold the URI address of the chosen image
    private Uri avatarImageURI;

    private StorageTask mAvatarStorageTask;

    // creating the impostors for the activity layout;
    EditText name_edit_text, surname_edit_text, email_edit_text, password_edit_text;
    Button login, cancel, register;
    ImageView choose_avatar_img;

    // creating a Register method which will be called to register the user to Firebase;
    public Register() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // linking the impostors to the buttons and editText boxes on the activity layout;
        login = findViewById(R.id.bt_login_2);
        cancel = findViewById(R.id.bt_cancel_2);
        register = findViewById(R.id.bt_register);
        name_edit_text = findViewById(R.id.txt_first_name);
        surname_edit_text = findViewById(R.id.txt_last_name);
        email_edit_text = findViewById(R.id.txt_email_2);
        password_edit_text = findViewById(R.id.txt_password_2);
        choose_avatar_img = findViewById(R.id.img_choose_avatar);

        // creating an instance of FirebaseDatabase with the reference to the USER node;
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        mStorageRef = FirebaseStorage.getInstance().getReference("images/avatar");

        choose_avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // creating a reference to FirebaseAuth;
        mAuth = FirebaseAuth.getInstance();

        // creating an onClickListener for the login impostor with an Intent to send the user to
        // the login activity layout if an user is already created;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Register.this, Login.class);
                startActivity(login);
            }
        });

        // creating an onClickListener for the cancel impostor to offer the chance to cancel
        // the registration process and sending the user back to the main activity layout where
        // only rear privileges are available for the user;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(Register.this, MainActivity.class);
                startActivity(cancel);
            }
        });

        // creating an onClickListener for the register impostor to start de process of registering
        // the user to the database;
        register.setOnClickListener(new View.OnClickListener() {
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

                                // email and password variables will save the input of the user from the
                                // activity layout;
                                String email = email_edit_text.getText().toString();
                                String password = password_edit_text.getText().toString();

                                // checking if the user actually typed the email and password;
                                // if not a message will appear on the screen prompting the user to do so;
                                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                                    Toast.makeText(getApplicationContext(), "Enter email and password",
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // name and surname variables will save the input from the user from the
                                // activity layout;
                                String name = name_edit_text.getText().toString();
                                String surname = surname_edit_text.getText().toString();
                                String avatarUri = downloadUrl.toString();

                                // creating a Users object containing all the data from the user;
                                user = new Users(name, surname, password, email, avatarUri);

                                // calling the registerUser parsing both ehe email and password from the user;
                                registerUser(email, password);
                            }
                        });
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

            Picasso.get().load(avatarImageURI).into(choose_avatar_img);
        }
    }

    // this method returns the extension of the image file that was selected;
    // ex: an image.jpg, the method wil return "jpg";
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void registerUser(String email, String password) {

        // the code inside the "registerUser" is offered by Android Studio by accessing
        // Tools > Firebase > Authentication > Sign up new users;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser) {
        mDatabase.child(currentUser.getUid()).setValue(user);
        Intent loginIntent = new Intent(this, MainActivity_logged_in.class);

//        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(
//                new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        loginIntent.putExtra("UID", currentUser.getUid());
//                        startActivity(loginIntent);
//                        Log.d(TAG, "registered and logged in");
//                    }
//                }
//        ).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "error registering and logging in");
//            }
//        });

        loginIntent.putExtra("UID", currentUser.getUid());
        startActivity(loginIntent);
    }
}