package com.example.song.myfirstapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteView extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap rvMap;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String rRouteId;
    private String AreaName;
    private String mUserId;
//    ArrayList<DataSnapshot> rRoutes=new ArrayList<>();
//    Map<String,DataSnapshot> qpoints=new HashMap<>();
    ArrayList<List<String>> point_info=new ArrayList<>();
//    ArrayList<String> points = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.rvMap);
        mapFrag.getMapAsync(this);
        if (rvMap != null) {
            rvMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override

                public void onMapLongClick(LatLng latLng) {
                    Geocoder geocoder = new Geocoder(RouteView.this);
                    List<Address> list;

                    try {
                        list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        return;
                    }

                }
            });
        }








        rRouteId = getIntent().getStringExtra("ResultRoute");

//        TextView RouteName=(TextView) findViewById(R.id.tRName2);
//        RouteName.setText(rRouteId);



        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUserId = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("agentRoutes").child(rRouteId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rName=dataSnapshot.child("Route Name").getValue().toString();
                TextView RouteName=(TextView) findViewById(R.id.tRName2);
                RouteName.setText(rName);

                String rDescript = dataSnapshot.child("Route Description").getValue().toString();
                TextView RouteDes = (TextView) findViewById(R.id.RouteView1).findViewById(R.id.rDescript);
                RouteDes.setText(rDescript);

                AreaName = dataSnapshot.child("Route tag").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.rvMap = map;

    }



    public void show_Markers(View v) throws IOException {

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(AreaName, 1);
        Address add = list.get(0);
            //String locality = add.getLocality();
        LatLng ll = new LatLng(add.getLatitude(), add.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 10);
            rvMap.moveCamera(update);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(AreaName)
                    .position(ll);
            Marker marker_o=rvMap.addMarker(markerOptions);
    }






}
