package com.example.song.myfirstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class profilename extends AppCompatActivity {
    private String mUserId;
    private String mUserEmail;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    ArrayList<String> routenamelist=new ArrayList<>();
    ArrayList<String> troutenamelist=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilename);

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
            final TextView profilePhone = (TextView) findViewById(R.id.vf).findViewById(R.id.traveler_phone);
            final TextView profileLocation = (TextView) findViewById(R.id.vf).findViewById(R.id.profile_location);
            final TextView profileHobbies = (TextView) findViewById(R.id.vf).findViewById(R.id.profile_hobbies);
            final TextView profileEmail = (TextView) findViewById(R.id.vf).findViewById(R.id.user_profile_email);
            final TextView name = (TextView) findViewById(R.id.vf).findViewById(R.id.name);

            final TextView agent_name=(TextView) findViewById(R.id.agent_profile_name);
            final TextView agent_email=(TextView) findViewById(R.id.agent_email);
            final TextView agent_phone=(TextView) findViewById(R.id.agent_phone);
            final TextView agent_hobbies=(TextView) findViewById(R.id.agent_hobby);

            final TextView traveler_name=(TextView) findViewById(R.id.traveler_profile_name);
            //final TextView traveler_email=(TextView) findViewById(R.id.traveler_email);
            //final TextView traveler_phone=(TextView) findViewById(R.id.traveler_phone);



            mDatabase.child("users").child(mUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String npName=snapshot.child("NickName").getValue().toString();
                    profileName.setText(npName);
                    name.setText("Name:"+npName);
                    agent_name.setText("Agent-"+npName);
                    traveler_name.setText("Traveler-"+npName);

                    String npPhone=snapshot.child("Telephone").getValue().toString();
                    profilePhone.setText("Telephone:"+npPhone);
                    agent_phone.setText(npPhone);
//                    traveler_phone.setText(npPhone);

                    String npLocation=snapshot.child("Location").getValue().toString();
                    profileLocation.setText("Location:"+npLocation);

                    String npHobby=snapshot.child("Hobby").getValue().toString();
                    profileHobbies.setText("Hobbies:"+npHobby);
                    agent_hobbies.setText("Hobbies:"+npHobby);

                    profileEmail.setText(mUserEmail);
                    agent_email.setText(mUserEmail);
//                    traveler_email.setText(mUserEmail);

                    for(String Rname:routenamelist){
                    showAgentRoutes(Rname);
                    }

                    for(String tRname:troutenamelist){
                        showTravlerRoutes(tRname);
                    }





                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });







//Get the routes created by current user as an agent and store the route name to routenamelist
            Query query=mDatabase.child("agentRoutes").orderByChild("Creater ID").equalTo(mUserId);
            ValueEventListener valueEventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        routenamelist.add(postSnapshot.child("Route Name").getValue().toString());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            };

            query.addValueEventListener(valueEventListener);


//Get the routes purchased by current user as an traveler and store the route name to troutenamelist
            Query query2=mDatabase.child("agentRoutes").orderByChild("Creater ID").equalTo(mUserId);
            ValueEventListener valueEventListener2 = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        troutenamelist.add(postSnapshot.child("Route Name").getValue().toString());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            };

            query2.addValueEventListener(valueEventListener2);



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





//Transfer to Traveler Route View page when clicking on The view button on traveler profile page
   public void buttonTravelerViewClick(View v){
       Intent intent1=new Intent(this,TravelerRouteView.class);
       this.startActivity(intent1);
   }


//Transfer to AddNewPoints page when clicking on Add New Button
    public void Addroutes(View v){
        Intent intentb=new Intent(this,AddNewPoints.class);
        startActivity(intentb);
    }


//Transfet to Edit Profile page when clicking on EditProfile button
    public void gotoEditProfile(View v) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent2 = new Intent(this, Change_Profile.class);
        startActivity(intent2);
    }



    //Add the routes provided by current agents to the agents profile by dynamically create RouteSegments
    public void showAgentRoutes(String RouteName){
        LinearLayout Agent_RouteView=(LinearLayout)findViewById(R.id.Agent_Routview);
        TextView Route=new TextView(this);
        RelativeLayout RouteSegment=new RelativeLayout(this);
        Button Edit=new Button(this);

        Route.setText(RouteName);
        Route.setTextColor(Color.parseColor("#3399FF"));
        Route.setTextSize(20);
        //Route.setPadding(10,0,0,0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, 30, 10, 0); //use ints here
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        Route.setLayoutParams(lp);


        Edit.setBackgroundColor(Color.parseColor("#3399FF"));
        Edit.setTextColor(Color.parseColor("#FFFFFF"));
        Edit.setText("Edit");
        Edit.setTextSize(18);
        Edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Intent activityChangeIntent = new Intent(profilename.this, EditRoute.class);
                Intent intent3=new Intent(getApplicationContext(),EditRoute.class);
                RelativeLayout p=(RelativeLayout) v.getParent();
                View R_name=p.getChildAt(0);
                TextView rn=(TextView) R_name;
                String rnstr=rn.getText().toString().trim();

                intent3.putExtra("CurrentRoute",rnstr);
                startActivity(intent3);

            }
        });
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp2.setMargins(0, 0, 10, 30); //use ints here
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        Edit.setLayoutParams(lp2);



        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp3.setMargins(100,20,100,20);

        RouteSegment.setLayoutParams(lp3);


        RouteSegment.addView(Route);
        RouteSegment.addView(Edit);
        Agent_RouteView.addView(RouteSegment);




    }




    //Add the routes purchased by current user to the travlers profile by dynamically create RouteSegments
    public void showTravlerRoutes(String RouteName){
        LinearLayout Agent_RouteView=(LinearLayout)findViewById(R.id.Traveler_Routview);
        TextView Route=new TextView(this);
        RelativeLayout RouteSegment=new RelativeLayout(this);
        Button Edit=new Button(this);

        Route.setText(RouteName);
        Route.setTextColor(Color.parseColor("#3399FF"));
        Route.setTextSize(20);
        //Route.setPadding(10,0,0,0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, 30, 10, 0); //use ints here
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        Route.setLayoutParams(lp);


        Edit.setBackgroundColor(Color.parseColor("#3399FF"));
        Edit.setTextColor(Color.parseColor("#FFFFFF"));
        Edit.setText("View");
        Edit.setTextSize(18);
        Edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Intent activityChangeIntent = new Intent(profilename.this, EditRoute.class);
                Intent intent4=new Intent(getApplicationContext(),TravelerRouteView.class);
                RelativeLayout p=(RelativeLayout) v.getParent();
                View R_name=p.getChildAt(0);
                TextView rn=(TextView) R_name;
                String rnstr=rn.getText().toString();

                intent4.putExtra("Travler Route",rnstr);
                startActivity(intent4);

            }
        });
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp2.setMargins(0, 0, 10, 30); //use ints here
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        Edit.setLayoutParams(lp2);



        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp3.setMargins(100,20,100,20);

        RouteSegment.setLayoutParams(lp3);


        RouteSegment.addView(Route);
        RouteSegment.addView(Edit);
        Agent_RouteView.addView(RouteSegment);




    }

}


