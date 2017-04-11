package com.example.song.myfirstapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditRoute extends AppCompatActivity {
    private GoogleMap mmMap;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    private String s;
    private String id;
    //private String Uid;
    ArrayList<String> points = new ArrayList<>();
    ArrayList<DataSnapshot> qroutes=new ArrayList<>();
    HashMap<String,DataSnapshot> qpoints=new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);
//        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.edit_route_map);
//        mapFrag.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if(mmMap != null) {
//            mmMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//                @Override
//
//                public void onMapLongClick (LatLng latLng){
//                    Geocoder geocoder = new Geocoder(EditRoute.this);
//                    List<Address> list;
//
//                    try {
//                        list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);}
//                    catch (IOException e) {
//                        return;
//                    }
//
//                }
//            });
//
//        }


        s = getIntent().getStringExtra("CurrentRoute");

        TextView RouteName=(TextView) findViewById(R.id.RName);
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
//                try {
//                    add_Markers();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });




        Query query=mDatabase.child("agentRoutes").orderByChild("Creater ID").equalTo(mUserId);
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    if (postSnapshot.child("Route Name").getValue().toString().equals(s)) {
                        qroutes.add(postSnapshot);
                        TextView des = (TextView) findViewById(R.id.RouteView).findViewById(R.id.Description);
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

        LinearLayout Agent_RouteView=(LinearLayout)findViewById(R.id.RouteView);
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
        Edit.setText("Edit");
        Edit.setTextSize(18);
        Edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent3=new Intent(getApplicationContext(),EditPoint.class);
                RelativeLayout p=(RelativeLayout) v.getParent();
                View R_name=p.getChildAt(0);
                TextView rn=(TextView) R_name;
                char pnstr=rn.getText().toString().charAt(7);
                String pn= pnstr+"";
                for (String k : qpoints.keySet()){
                    if (k.equals(pn)){
                        id = qpoints.get(k).getKey();
                    }
                }
                intent3.putExtra("CurrentRoute",s);
                intent3.putExtra("CurrentPoint",pn);
                intent3.putExtra("pointid",id);
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
        lp3.setMargins(100,5,100,5);

        RouteSegment.setLayoutParams(lp3);


        RouteSegment.addView(Route);
        RouteSegment.addView(Edit);
        Agent_RouteView.addView(RouteSegment);




    }

//    public void add_Markers() throws IOException {
//        int n=point_info.size();
//        for(int i=0;i<n;i++){
//            String number=point_info.get(i).get(0);
//            String location=point_info.get(i).get(1);
//            String descript=point_info.get(i).get(2);
//            Geocoder geocoder = new Geocoder(this);
//            List<Address> list = geocoder.getFromLocationName(location, 1);
//            Address add = list.get(0);
//            //String locality = add.getLocality();
//            LatLng ll = new LatLng(add.getLatitude(), add.getLongitude());
//            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
//            mMap.moveCamera(update);
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.title("Point "+number+": "+location)
//                    .position(ll);
//            Marker marker_o=mMap.addMarker(markerOptions);
//        }
//
//
//
//
//
//
//
//    }





}




