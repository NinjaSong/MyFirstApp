package com.example.song.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.widget.ViewFlipper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class profilename extends AppCompatActivity {
    private TextView mTextMessage;
    private String mUserId;
    private String mUserEmail;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilename);


        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        vf.setDisplayedChild(0);


        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            Intent intentlg=new Intent(this,LogInActivity.class);
            this.startActivity(intentlg);
        } else {
            mUserId = mFirebaseUser.getUid();
            mUserEmail=mFirebaseUser.getEmail();
            final TextView profileName = (TextView) findViewById(R.id.vf).findViewById(R.id.user_profile_name);
            final TextView profilePhone = (TextView) findViewById(R.id.vf).findViewById(R.id.phone);
            final TextView profileLocation = (TextView) findViewById(R.id.vf).findViewById(R.id.profile_location);
            final TextView profileHobbies = (TextView) findViewById(R.id.vf).findViewById(R.id.profile_hobbies);
            final TextView profileEmail = (TextView) findViewById(R.id.vf).findViewById(R.id.user_profile_email);
            final TextView name = (TextView) findViewById(R.id.vf).findViewById(R.id.name);



            mDatabase.child("users").child(mUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String npName=snapshot.child("NickName").getValue().toString();
                    profileName.setText(npName);
                    name.setText("Name:"+npName);
                    String npPhone=snapshot.child("Telephone").getValue().toString();
                    profilePhone.setText("Telephone:"+npPhone);
                    String npLocation=snapshot.child("Location").getValue().toString();
                    profileLocation.setText("Location:"+npLocation);
                    String npHobby=snapshot.child("Hobby").getValue().toString();
                    profileHobbies.setText("Hobbies:"+npHobby);
                    profileEmail.setText(mUserEmail);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });




        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    vf.setDisplayedChild(0);
                    return true;
                case R.id.navigation_agent:
                    vf.setDisplayedChild(1);
                    return true;
                case R.id.navigation_travler:
                    vf.setDisplayedChild(2);
                    return true;
            }
            return false;
        }

    };






   public void buttonTravelerViewClick(View v){
       Intent intent1=new Intent(this,TravelerRouteView.class);
       this.startActivity(intent1);
   }

    public void Addroutes(View v){
        Intent intentb=new Intent(this,AddNewPoints.class);
        startActivity(intentb);
    }

    public void buttonAgentEditClick(View v){
        Intent intent3=new Intent(this,EditRoute.class);
        this.startActivity(intent3);
    }



    public void gotoEditProfile(View v) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent2 = new Intent(this, Change_Profile.class);
        startActivity(intent2);
    }

}


