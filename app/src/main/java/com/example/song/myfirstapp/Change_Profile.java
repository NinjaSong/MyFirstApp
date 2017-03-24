package com.example.song.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Change_Profile extends AppCompatActivity {
    private Button sbchangesb;
    private EditText mTel;
    private EditText mEmail;
    private EditText mLocation;
    private EditText mHobby;
    private EditText mPassword;
    private TextView username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference mRootRef = database.getReference("https://travelmash-49f51.firebaseio.com/");
//        sbchangesb=(Button) findViewById(R.id.sub_changes_b);
//        mTel=(EditText) findViewById(R.id.edittel);
//        mEmail=(EditText) findViewById(R.id.editemail);
//        mLocation=(EditText) findViewById(R.id.editlocation);
//        mHobby=(EditText) findViewById(R.id.edithobby);
//        mPassword=(EditText) findViewById(R.id.password);
////        username=(TextView) findViewById(R.id.user_profile_name);
//        sbchangesb.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                String vpassword=mPassword.getText().toString();
//                String vTel=mTel.getText().toString();
//                String vEmail=mEmail.getText().toString();
//                String vLocation=mLocation.getText().toString();
//                String vHobby=mHobby.getText().toString();
//                String vusername="Eddie";
//                DatabaseReference childRef=mRootRef.child(vusername);
//                DatabaseReference childTel=childRef.child("Telephone");
//                childTel.setValue(vTel);
//                DatabaseReference childEmail=childRef.child("Email");
//                childEmail.setValue(vEmail);
//                DatabaseReference childLocation=childRef.child("Location");
//                childLocation.setValue(vLocation);
//                DatabaseReference childHobby=childRef.child("Hobby");
//                childHobby.setValue(vHobby);
//                DatabaseReference childPassword= childRef.child("Password");
//                childPassword.setValue(vpassword);
//                FirebaseDatabase.getInstance().goOffline();
//
//
//
//            }
//        });
    }

}