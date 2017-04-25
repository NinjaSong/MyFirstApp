package com.example.song.myfirstapp;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TravelerRouteView extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    private String s;
    private String id;
    ArrayList<String> points = new ArrayList<>();
    ArrayList<DataSnapshot> qroutes=new ArrayList<>();
    HashMap<String,DataSnapshot> qpoints=new HashMap<>();
    ArrayList<List<String>> point_info=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveler_route_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.traveler_viewmap);
        mapFrag.getMapAsync(this);
        if(mMap != null) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override

                public void onMapLongClick (LatLng latLng){
                    Geocoder geocoder = new Geocoder(TravelerRouteView.this);
                    List<Address> list;

                    try {
                        list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);}
                    catch (IOException e) {
                        return;
                    }

                }
            });

        }

        s = getIntent().getStringExtra("Traveler Route");

        TextView RouteName=(TextView) findViewById(R.id.tRName2);
        RouteName.setText(s);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUserId = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        mDatabase.child("Points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot d:qroutes){
                    searchPoints(d.getKey());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });




        Query query=mDatabase.child("agentRoutes").orderByChild("Creater ID");
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    if (postSnapshot.child("Route Name").getValue().toString().equals(s)) {
                        qroutes.add(postSnapshot);
                        TextView des = (TextView) findViewById(R.id.tDesc);
                        des.setText(postSnapshot.child("Route Description").getValue().toString());



                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };

        query.addValueEventListener(valueEventListener);

    }
    @Override
    public void onMapReady(final GoogleMap map) {
        this.mMap = map;

    }






    public void searchPoints(String Rid){
        Query query=mDatabase.child("Points").orderByChild("RouteId").equalTo(Rid);
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {

                    String num = postSnapshot.child("Number").getValue().toString();
                    qpoints.put(num,postSnapshot);
                    String addr = postSnapshot.child("Address").getValue().toString();
                    String pointdes = postSnapshot.child("Point Description").getValue().toString();

                    List<String> info=new ArrayList<>();
                    info.add(num);
                    info.add(addr);
                    info.add(pointdes);
                    point_info.add(info);


                    points.add("Point: "+num+"\n"+"Address: "+addr+"\n"+"Description: "+pointdes);
                }

                for (String i : points){
                    showPoints(i);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };

        query.addValueEventListener(valueEventListener);

    }

    public void showPoints(String Pointinfo){

        LinearLayout Agent_RouteView=(LinearLayout)findViewById(R.id.TViewLayout);
        TextView Route=new TextView(this);
        RelativeLayout RouteSegment=new RelativeLayout(this);
        Button Edit=new Button(this);

        Route.setText(Pointinfo);
        Route.setTextColor(Color.parseColor("#3399FF"));
        Route.setTextSize(15);
        //Route.setPadding(10,0,0,0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, 30, 10, 0); //use ints here
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        Route.setLayoutParams(lp);





        Edit.setBackgroundColor(Color.parseColor("#3399FF"));
        Edit.setTextColor(Color.parseColor("#FFFFFF"));
        Edit.setText("Info");
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
        lp3.setMargins(100,5,100,5);

        RouteSegment.setLayoutParams(lp3);


        RouteSegment.addView(Route);
        RouteSegment.addView(Edit);
        Agent_RouteView.addView(RouteSegment);




    }

    public void add_Markers2(View v) throws IOException {
        int n=qpoints.size();
        for(int i=0;i<n;i++){
            String number=point_info.get(i).get(0);
            String location=point_info.get(i).get(1);
            String descript=point_info.get(i).get(2);
            Geocoder geocoder = new Geocoder(this);
            List<Address> list = geocoder.getFromLocationName(location, 1);
            Address add = list.get(0);
            //String locality = add.getLocality();
            LatLng ll = new LatLng(add.getLatitude(), add.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 10);
            mMap.moveCamera(update);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("Point "+number+": "+location)
                    .position(ll);
            Marker marker_o=mMap.addMarker(markerOptions);
        }







    }





}

