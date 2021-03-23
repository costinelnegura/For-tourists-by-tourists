package com.costinel.fortouristsbytourists;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import com.costinel.fortouristsbytourists.Model.Attraction;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;
        import java.util.List;

        // creating ImagesActivity class to enable the app to access the data from the Firebase
        // database and show it in the recyclerView;
public class ImagesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Attraction> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        // linking the impostor to the activity layout declaring it to have a fixed size
        // and linear with the context in this case "this";
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        // mDatabaseRef takes the data from the attractions node on Firebase;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("attractions");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            // this method assures the app that it extracts the data from the node
            // using dataSnapshot;
            // a new Upload object is created get the data as Upload.class;
            // the new object is saved in a new ArrayList mUploads;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // using the for loop to go through all the children nodes from the attraction node;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Attraction upload = postSnapshot.getValue(Attraction.class);
                    mUploads.add(upload);
                }

                // creating the image adapter;
                mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);

                // parsing the image adapter to the recyclerView and making the
                // progress circle invisible;
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            // this method will be called if the upload is unsuccessful;
            // ex: no permission to read from the database;
            // making the progress circle visible;
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(),
                         Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
}