package com.costinel.fortouristsbytourists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.ArrayList;
import java.util.List;

public class ConfirmPassword extends AppCompatActivity {

    private Button resetPassword;
    private EditText typeNewPassword, reTypeNewPassword;
    private TextView mUserEmail;

    private String userPassword;
    private String newPassword;
    private String userEmail;
    private String Uid;
    private List<Users> userList;


    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        resetPassword = findViewById(R.id.bt_reset_password);
        typeNewPassword = findViewById(R.id.txt_type_new_password_check_1);
        reTypeNewPassword = findViewById(R.id.txt_type_new_password_check_2);
        mUserEmail = findViewById(R.id.txt_user_email);





//        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).
//                addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        Log.i("ConfirmPassword", "Received the reset password dynamic link!");
//
//                        //get the deep link from result (may be null if no link is found)
//                        Uri deepLink = null;
//                        if(pendingDynamicLinkData != null){
//                            deepLink = pendingDynamicLinkData.getLink();
//                            String email = deepLink.getQueryParameter("email");
//
//                            //to optimise
////                    userEmail.setText(email);
//                        }
//
//                        //handle the deep link by extracting the page relevant here
//                        if(deepLink != null){
//                            Log.i("ConfirmPassword", "The captured deep link: " + deepLink.toString());
//                        }
//                    }
//                }).addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.i("ConfirmPassword", "Failed to receive the reset password dynamic link");
//            }
//        });


        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                while (!typeNewPassword.getText().toString().equals(reTypeNewPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Entered passwords not matching",
                            Toast.LENGTH_LONG).show();
                }

                userList = new ArrayList<>();
                userEmail = mUserEmail.getText().toString();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("user");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshop : snapshot.getChildren()) {
                            Users user = postSnapshop.getValue(Users.class);
//                            userList.add(user);
                            if(user.getEmail().equals(userEmail)){
                                userPassword = user.getPassword();
                                newPassword = typeNewPassword.getText().toString();
                                changePassword(userEmail, userPassword);



                                //fix password change, it changes the password of the wrong user.





                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("ConfirmPassword", "User was not found while searching firebase");
                    }
                });

//                for(int i = 0; i < userList.size(); i++){
//                    if(userList.get(i).getEmail().equals(userEmail)){
//                        changePassword(userList.get(i).getEmail(), userList.get(i).getPassword());
//                    }
//                }




                //calling the change password method where the user is authenticated
                //and the password is changed in Firebase auth and the user is updated in
                // realtime database.


//                Intent i = new Intent(ConfirmPassword.this, Login.class);
//                i.putExtra("email", userEmail);
//                i.putExtra("password", userPassword);
//                startActivity(i);
            }
        });
    }

    private void changePassword(String email, String password) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credentials = EmailAuthProvider.getCredential(email, password);
        Log.i("Credentials", "credentials stage reached");


        firebaseUser.reauthenticate(credentials).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("ConfirmPassword", "User re-authenticated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ConfirmPassword", "User not re-authenticated");
            }
        });

        Uid = firebaseUser.getUid();

        //this section will update the password of the user in Firebase Auth
        firebaseUser.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("ConfirmPassword", "password changed successfully");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ConfirmPassword", "password not changed");
            }
        });



        //setting the new password to the user password field in the database.
        mDatabaseRef.child(Uid).child("password").setValue(newPassword);
    }

}