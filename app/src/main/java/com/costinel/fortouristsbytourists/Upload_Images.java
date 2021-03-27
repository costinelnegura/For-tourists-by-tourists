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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.costinel.fortouristsbytourists.AttractionLoader.ImageAdapter;
import com.costinel.fortouristsbytourists.Model.Attraction;
import com.costinel.fortouristsbytourists.SelectedImagesLoader.SelectedImageAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class Upload_Images extends AppCompatActivity {

    // creating a constant to use it to identify the image request;
    private static final int PICK_IMAGE_REQUEST = 1;

    // creating the impostors for the upload images activity layout;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText AttractionName, AttractionLocation, AttractionDescription, AttractionPrice;
    private RecyclerView imageRecyclerView;
    private ProgressBar mProgressBar;
    private SelectedImageAdapter mAdapter;

    // creating the Firebase variables to connect the class to the storage and database.
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    //List interface for an ArrayList to store the local stored url addresses of the images
    private List<Uri> tempUri = new ArrayList<>();

    //this stores the attraction id created once the upload button is pressed
    private String uploadId;
    private String userUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        // linking the impostors  to the ones on the activity layout;
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.bt_create_attraction);
        AttractionName = findViewById(R.id.edit_attraction_name);
        AttractionLocation = findViewById(R.id.edit_attraction_location);
        AttractionDescription = findViewById(R.id.edit_attraction_description);
        AttractionPrice = findViewById(R.id.edit_attraction_price);
        imageRecyclerView = findViewById(R.id.recycle_view_upload_images);
        mProgressBar = findViewById(R.id.progress_bar);


        userUid = getIntent().getStringExtra("UID");


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
                    if (!tempUri.isEmpty()) {
                        uploadAttraction();
                    } else {
                        // if no file is selected, a message will let the user know;
                        Toast.makeText(getApplicationContext(), "No image selected!", Toast.LENGTH_SHORT).show();
                    }
                }
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

            //selected image is added to the List (ArrayList)
            tempUri.add(data.getData());
            createRecyclerView();
        }
    }

    //method to create the recyclerview, by setting its layout grid and parsing the uri addresses
    // to the adapter to be placed in the recyclerview
    public void createRecyclerView() {
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3,
                LinearLayoutManager.VERTICAL, false));

        //print selected image to the screen
        mAdapter = new SelectedImageAdapter(Upload_Images.this, tempUri);
        imageRecyclerView.setAdapter(mAdapter);
    }

    // method to store the images which uri's are coming from an ArrayList, to firebase and use the
    // url captured to create references in the created attraction
    private void storeImage(Uri uri) {
        // creating another storage reference which is = to the one that points to the
        // upload path in Firebase, and its child will have the name formed of
        // the current time in milliseconds and then the file extension;
        // ex: 1606688319419.img
        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + ".img");

        mUploadTask = fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // to obtain the correct URL from the uri, Task<> is used and the data
                // is extracted from the taskSnapshot;
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                Uri uri = (urlTask.getResult());
                mDatabaseRef.child(uploadId).child("images").child(System.currentTimeMillis() + "img")
                        .setValue(uri.toString());
            }
        });
    }

    // method to create the attraction with multiple images
    private void uploadAttraction() {
        if(AttractionName.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Specify attraction name!", Toast.LENGTH_LONG).show();
        }else if(AttractionLocation.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Specify attraction location!", Toast.LENGTH_LONG).show();
        }else if(AttractionDescription.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Specify attraction description!", Toast.LENGTH_LONG).show();
        }else if(AttractionPrice.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Specify attraction price!", Toast.LENGTH_LONG).show();
        }else if(tempUri.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Add image(s)!", Toast.LENGTH_LONG).show();
        }else{
            Attraction attraction = new Attraction(AttractionName.getText().toString().trim(),
                    AttractionLocation.getText().toString().trim(),
                    AttractionDescription.getText().toString().trim(),
                    AttractionPrice.getText().toString().trim()
            );

            uploadId = mDatabaseRef.push().getKey();
            mDatabaseRef.child(uploadId).setValue(attraction);

            //this loop sends the local uri addresses from the ArrayList to the storeImage method
            for (int i = 0; i < tempUri.size(); i++) {
                storeImage(tempUri.get(i));
            }
            Toast.makeText(getApplicationContext(), "Attraction created!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Upload_Images.this, MainActivity.class);
            i.putExtra("logged", true);
            i.putExtra("UID", userUid);
            startActivity(i);
        }
    }
}