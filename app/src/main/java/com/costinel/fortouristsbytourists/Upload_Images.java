package com.costinel.fortouristsbytourists;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.costinel.fortouristsbytourists.Model.Attraction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Upload_Images extends AppCompatActivity {

    // creating a constant to use it to identify the image request;
    private static final int PICK_IMAGE_REQUEST = 1;

    // creating the impostors for the upload images activity layout;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView Finish;
    private EditText AttractionName, AttractionLocation, AttractionDescription, AttractionPrice;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    // creating a variable that will point to the selected image;
    private Uri mImageUri;

    // creating the Firebase variables to connect the class to the storage and database.
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    private Map<String, String> selectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        // linking the impostors  to the ones on the activity layout;
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        Finish = findViewById(R.id.text_finish);
        AttractionName = findViewById(R.id.edit_attraction_name);
        AttractionLocation = findViewById(R.id.edit_attraction_location);
        AttractionDescription = findViewById(R.id.edit_attraction_description);
        AttractionPrice = findViewById(R.id.edit_attraction_price);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        // designating the 2 variables to getInstance and getReference from Firebase;
        // mStorageRef has the patch "images/attractions" because there are stored all the
        // attractions images;
        // mDatabaseRef has the path to "attractions" because there are stored all the attractions
        // information;
        mStorageRef = FirebaseStorage.getInstance().getReference("images/attractions");
        //mDatabaseRef as a reference of the attraction object
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attractions");


        // creating an on click listener to mButtonChooseImage;
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // creating an on click listener for mButtonUpload
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the task is not null an if it is in progress to show a message
                // to the user or otherwise uploading the image, which prevents the use to
                // upload the same image multiple times;
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Upload_Images.this, "Upload in progress...",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (mImageUri != null) {
                        uploadFile();
                        Toast.makeText(getApplicationContext(), "Attraction created!", Toast.LENGTH_LONG).show();
                    }else{
                        // if no file is selected, a message will let the user know;
                        Toast.makeText(getApplicationContext(), "No image selected!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // creating an on click listener to Finish which will take the user to the main paged where
        // he is logged in;
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Upload_Images.this,
                        MainActivity_logged_in.class);
                startActivity(i);
//                openImagesActivity();
            }
        });
    }

    // creating a method for when the mButtonChooseImage is clicked to choose ONLY an image type
    // file;
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // this method checks if the result is matching the request code with the result code,
    // and if data, data.getData() is not null to verify if the image is ok to select;
    // image is then loaded into the impostor variable mImageView;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    // this method returns the extension of the image file that was selected;
    // ex: an image.jpg, the method wil return "jpg";
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // creating the upload method which will upload the selected image to Firebase;
    private void uploadFile() {

            // creating another storage reference which is = to the one that points to the
            // upload path in Firebase, and its child will have the name formed of
            // the current time in milliseconds and then the file extension;
            // ex: 1606688319419.jpeg
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    // creating an onSuccess method which will upload the object created of the
                    // image selected and all the details tipped by the user to Firebase;
                    // this method resets the progress bar to 0 with a 500 milliseconds delay,
                    // using a handler;
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            // to obtain the correct URL from the uri, Task<> is used and the data
                            // is extracted from the taskSnapshot;
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();


                            Attraction upload = new Attraction(AttractionName.getText().toString().trim(),
                                    AttractionLocation.getText().toString().trim(),
                                    AttractionDescription.getText().toString().trim(),
                                    AttractionPrice.getText().toString().trim(),
                                    selectedImages
                            );

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    // creating an onFailure method to display an error message if the upload
                    // is not successful;
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Upload_Images.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    // the progress listener will update the progress bar on the upload_images
                    // activity layer by extracting the data from the taskSnapshot;
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                                    taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
    }
}