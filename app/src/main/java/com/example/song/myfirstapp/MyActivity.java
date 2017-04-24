package com.example.song.myfirstapp;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    ArrayList<DataSnapshot> qroutelist = new ArrayList<>();
    //private String mUserId;
    //private String routeId;
    private String inputCity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.citymap);
        mapFrag.getMapAsync(this);
        if (mMap != null) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override

                public void onMapLongClick(LatLng latLng) {
                    Geocoder geocoder = new Geocoder(MyActivity.this);
                    List<Address> list;

                    try {
                        list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        return;
                    }

                }
            });
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.mMap = map;

    }


    public void submitCity(View v) throws IOException {
        //qroutelist.clear();
        TextView cityname = (TextView) findViewById(R.id.CityName);
        String cName = cityname.getText().toString();
        inputCity = cName;
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(cName, 1);
        Address add = list.get(0);
        //String locality = add.getLocality();
        LatLng ll = new LatLng(add.getLatitude(), add.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
        mMap.moveCamera(update);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(cName)
                .position(ll);
        Marker marker_o = mMap.addMarker(markerOptions);


        //Initialize Firebase Auth and Database Reference
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseUser = mFirebaseAuth.getCurrentUser();
//        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            Intent intentlg = new Intent(this, LogInActivity.class);
            this.startActivity(intentlg);
        } else {
            Query query = mDatabase.child("agentRoutes").orderByChild("Route tag").equalTo(inputCity);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        qroutelist.add(postSnapshot);

                    }

                }

                public void onCancelled(DatabaseError databaseError)
                {

                }
            };

            query.addValueEventListener(valueEventListener);


            mDatabase.child("agentRoutes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot n:qroutelist){
                        showQualifiedRoutes(n);
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.miProfile) {
            Intent intent1 = new Intent(this,profilename.class);
            this.startActivity(intent1);
            return true;
        }

        if (id == R.id.action_settings) {
            Intent intent1 = new Intent(this,settingactivity.class);
            this.startActivity(intent1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //Add the routes provided by current agents to the agents profile by dynamically create RouteSegments
    public void showQualifiedRoutes(DataSnapshot d){
        LinearLayout Agent_RouteView=(LinearLayout)findViewById(R.id.my_Linear);
        TextView Route=new TextView(this);
        RelativeLayout RouteSegment=new RelativeLayout(this);
        Button V=new Button(this);

        Route.setText(d.child("Route Name").getValue().toString());
        Route.setTextColor(Color.parseColor("#3399FF"));
        Route.setTextSize(20);
        //Route.setPadding(10,0,0,0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, 30, 10, 0); //use ints here
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        Route.setLayoutParams(lp);


        V.setBackgroundColor(Color.parseColor("#3399FF"));
        V.setTextColor(Color.parseColor("#FFFFFF"));
        V.setText("View");
        V.setTextSize(18);
        V.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Intent activityChangeIntent = new Intent(profilename.this, EditRoute.class);
                Intent intent3=new Intent(getApplicationContext(),RouteView.class);
                RelativeLayout p=(RelativeLayout) v.getParent();
                View R_name=p.getChildAt(0);
                TextView rn=(TextView) R_name;
                String rnstr=rn.getText().toString().trim();
                for(DataSnapshot pp:qroutelist){
                    if(pp.child("Route Name").getValue().toString().equals(rnstr)){
                        String rRouteId=pp.getKey();
                        intent3.putExtra("ResultRoute",rRouteId);

                    }
                }

                startActivity(intent3);

            }
        });
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp2.setMargins(0, 0, 10, 30); //use ints here
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        V.setLayoutParams(lp2);



        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp3.setMargins(100,20,100,20);

        RouteSegment.setLayoutParams(lp3);


        RouteSegment.addView(Route);
        RouteSegment.addView(V);
        Agent_RouteView.addView(RouteSegment);




    }



}
