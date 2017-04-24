package com.example.song.myfirstapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhangqinlan on 4/5/17.
 */

public class AddNewPoints extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    private EditText routeName;
    private EditText routedescription;
    private EditText routePrice;
    private EditText markeraddress;
    private EditText markerdescription;
    private EditText routetag;
    private EditText markernumber;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    private String routeId;


    Map<String,Marker> markermap=new HashMap<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_routes);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFrag.getMapAsync(this);
        routeId = generateUID();


        if(mMap != null) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override

                public void onMapLongClick (LatLng latLng){
                    Geocoder geocoder = new Geocoder(AddNewPoints.this);
                    List<Address> list;

                    try {
                        list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);}
                    catch (IOException e) {
                        return;
                    }

                }
            });
        }
    }
    @Override
    public void onMapReady(final GoogleMap map) {
        this.mMap = map;

    }

    public String generateUID(){
        return UUID.randomUUID().toString();
    }


    public void findLocation(View v) throws IOException {

        EditText et = (EditText)findViewById(R.id.markeraddress1);
        String location = et.getText().toString();
        EditText mknumber2=(EditText) findViewById(R.id.markernumber1);
        String mknum2=mknumber2.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(location, 1);
        Address add = list.get(0);
        //String locality = add.getLocality();
        LatLng ll = new LatLng(add.getLatitude(), add.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
        mMap.moveCamera(update);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Point "+mknum2+": "+location)
                .position(ll);

        //When the marker number has already existed, remove the previous one
        if(markermap.get(mknum2)!=null)

        {
            markermap.get(mknum2).remove();
        }

        Marker marker_o=mMap.addMarker(markerOptions);
        markermap.put(mknum2, marker_o);
        addPoints();

    }




    public void addPoints() {
        String pointId = generateUID();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            Intent intentlg = new Intent(this, LogInActivity.class);
            this.startActivity(intentlg);
        } else {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mUserId = mFirebaseUser.getUid();
            markeraddress = (EditText) findViewById(R.id.markeraddress1);
            markerdescription = (EditText) findViewById(R.id.pointdescript1);
            markernumber=(EditText) findViewById(R.id.markernumber1);


            String mkaddress = markeraddress.getText().toString();
            String mkdes = markerdescription.getText().toString();
            String mknum=markernumber.getText().toString();


            DatabaseReference childRef = mRootRef.child("Points").child(pointId);

            DatabaseReference childcreaterId = childRef.child("RouteId");
            childcreaterId.setValue(routeId);

            DatabaseReference childPoint = childRef.child("Number");
            childPoint.setValue(mknum);

            DatabaseReference childMarkerAddr = childRef.child("Address");
            childMarkerAddr.setValue(mkaddress);

            DatabaseReference childmkdes = childRef.child("Point Description");
            childmkdes.setValue(mkdes);

            //After submit changes turn to the main page with map


        }
    }


    public void finishRoute(View v){

        PolylineOptions route=new PolylineOptions();
        int n=markermap.size();
        ArrayList<Integer> mapkeys=new ArrayList<>();
        double sumLat=0;
        double sumLng=0;
        for(Marker e:markermap.values()){
            sumLat+=e.getPosition().latitude;
            sumLng+=e.getPosition().longitude;
        }
        for(String k: markermap.keySet()){
            mapkeys.add(Integer.valueOf(k));
        }
        Collections.sort(mapkeys);

        //Sort the key of the markermap which is the markernumber, and create polyline according to the order of the number;
        for(int k:mapkeys){
            Marker m=markermap.get(String.valueOf(k));
            route.add(m.getPosition()).width(5).color(Color.RED);

        }
        Polyline line=mMap.addPolyline(route);

        LatLng center = new LatLng(sumLat/n, sumLng/n);
        CameraUpdate mapupdate = CameraUpdateFactory.newLatLngZoom(center, 9);
        mMap.moveCamera(mapupdate);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            Intent intentlg = new Intent(this, LogInActivity.class);
            this.startActivity(intentlg);
        } else {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mUserId = mFirebaseUser.getUid();
            routeName = (EditText) findViewById(R.id.Routename);
            routedescription = (EditText) findViewById(R.id.route_descript1);
            routetag = (EditText) findViewById(R.id.routetag1);
            routePrice=(EditText) findViewById(R.id.routeprice1);

            String mrouteName = routeName.getText().toString();
            String routedes = routedescription.getText().toString();
            String rttag = routetag.getText().toString();
            String rprice=routePrice.getText().toString();

            DatabaseReference childRouteRf = mRootRef.child("agentRoutes").child(routeId);

            DatabaseReference childRouteName = childRouteRf.child("Route Name");
            childRouteName.setValue(mrouteName);

            DatabaseReference childRoutePrice=childRouteRf.child("Route Price");
            childRoutePrice.setValue(rprice);

            DatabaseReference childRouteCreate = childRouteRf.child("Creater ID");
            childRouteCreate.setValue(mUserId);

            DatabaseReference childRoutedes = childRouteRf.child("Route Description");
            childRoutedes.setValue(routedes);

            DatabaseReference childRoutetag = childRouteRf.child("Route tag");
            childRoutetag.setValue(rttag);

        }


    }


}




