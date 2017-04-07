package com.example.song.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Change_Profile extends AppCompatActivity {
    private Button sbchangesb;
    private EditText mTel;
    private EditText mNickname;
    private EditText mLocation;
    private EditText mHobby;
    private EditText mPassword;
    public static final String TAG="Change_Profile";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sbchangesb=(Button) findViewById(R.id.sub_changes_b);
        sbchangesb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                submitChanges();
                updatePassword();

            }
        });
    }

    public void submitChanges() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            Intent intentlg = new Intent(this, LogInActivity.class);
            this.startActivity(intentlg);
        } else {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mUserId = mFirebaseUser.getUid();
            mTel = (EditText) findViewById(R.id.edittel);
            mNickname = (EditText) findViewById(R.id.editnickname);
            mLocation = (EditText) findViewById(R.id.editlocation);
            mHobby = (EditText) findViewById(R.id.edithobby);
            mPassword = (EditText) findViewById(R.id.password);


//            String vpassword = mPassword.getText().toString();
            String vTel = mTel.getText().toString();
            String vNickname = mNickname.getText().toString();
            String vLocation = mLocation.getText().toString();
            String vHobby = mHobby.getText().toString();


            DatabaseReference childRef = mRootRef.child("users").child(mUserId);

            DatabaseReference childTel = childRef.child("Telephone");
            childTel.setValue(vTel);
            DatabaseReference childEmail = childRef.child("NickName");
            childEmail.setValue(vNickname);
            DatabaseReference childLocation = childRef.child("Location");
            childLocation.setValue(vLocation);
            DatabaseReference childHobby = childRef.child("Hobby");
            childHobby.setValue(vHobby);
//            DatabaseReference childPassword = childRef.child("Password");
//            childPassword.setValue(vpassword);

            updatePassword();

//        After submit changes turn to the main page with map
            Intent intent = new Intent(this, MyActivity.class);
            this.startActivity(intent);


        }
    }

    public void updatePassword(){
        mPassword=(EditText) findViewById(R.id.password);
        String vpassword=mPassword.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(vpassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                        else{
                            Log.d(TAG, "User password updated.");

                        }
                    }
                });

    }

}