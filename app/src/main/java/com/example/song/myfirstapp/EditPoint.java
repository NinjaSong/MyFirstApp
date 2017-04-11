package com.example.song.myfirstapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import java.util.Map;


public class EditPoint extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String routeName;
    private String pointNumber;
    private String routeid;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    Map<String,Marker> markermap=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_point);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.edit_point_map);
        mapFrag.getMapAsync(this);
        if(mMap != null) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override

                public void onMapLongClick (LatLng latLng){
                    Geocoder geocoder = new Geocoder(EditPoint.this);
                    List<Address> list;

                    try {
                        list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);}
                    catch (IOException e) {
                        return;
                    }

                }
            });

        }


        routeName = getIntent().getStringExtra("CurrentRoute");
        pointNumber=getIntent().getStringExtra("CurrentPoint");
        routeid = getIntent().getStringExtra("pointid");

        TextView RName=(TextView) findViewById(R.id.EpointRN);
        RName.setText(routeName);
        TextView PNumber=(TextView) findViewById(R.id.markernumber1);
        PNumber.setText("Point "+pointNumber);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUserId = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Points").child(routeid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mkAddr=dataSnapshot.child("Address").getValue().toString();
                String mkDesc=dataSnapshot.child("Point Description").getValue().toString();
                TextView mAddr=(TextView) findViewById(R.id.markeraddress);
                mAddr.setText(mkAddr);
                TextView des=(TextView) findViewById(R.id.markerdescrypt);
                des.setText(mkDesc);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onMapReady(final GoogleMap map) {
        this.mMap = map;

    }







    public void buttonUpdate(View v) throws IOException {
        //update in firebase
        TextView newAddress=(TextView) findViewById(R.id.newmarkeraddr);
        String naddr=newAddress.getText().toString();
        TextView newDesc=(TextView) findViewById(R.id.newpointdesc);
        String ndesc=newDesc.getText().toString();


        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(naddr, 1);
        Address add = list.get(0);
        //String locality = add.getLocality();
        LatLng ll = new LatLng(add.getLatitude(), add.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
        mMap.moveCamera(update);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(naddr)
                .position(ll);


        if(markermap.get(routeid)!=null)

        {
            markermap.get(routeid).remove();
        }

        Marker marker_o=mMap.addMarker(markerOptions);
        markermap.put(routeid, marker_o);


        mDatabase.child("Points").child(routeid).child("Address").setValue(naddr);
        mDatabase.child("Points").child(routeid).child("Point Description").setValue(ndesc);
//        Intent intent1 = new Intent(this,EditRoute.class);
//        this.startActivity(intent1);

    }

}
