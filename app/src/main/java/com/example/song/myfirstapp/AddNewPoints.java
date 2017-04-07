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
        import java.util.List;

/**
 * Created by zhangqinlan on 4/5/17.
 */

public class AddNewPoints extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    Marker marker;
    private EditText routeName;
    private EditText routedescription;
    private EditText markeraddress;
    private EditText markerdescription;
    private EditText routetag;
    private EditText markernumber;
    //private Button AddButton;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    ArrayList<LatLng> markerList=new ArrayList<>();
    PolylineOptions route=new PolylineOptions();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_routes);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFrag.getMapAsync(this);


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
        if(marker != null)
            marker.remove();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Point "+mknum2+": "+location)
                     .position(new LatLng(add.getLatitude(), add.getLongitude()));
        markerList.add(ll);
        mMap.addMarker(markerOptions);

        route.add(ll).width(5).color(Color.RED);
        Polyline line=mMap.addPolyline(route);




    }

    public void addPoints(View v) {
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
            markeraddress = (EditText) findViewById(R.id.markeraddress1);
            markerdescription = (EditText) findViewById(R.id.pointdescript1);
            routetag = (EditText) findViewById(R.id.routetag1);
            markernumber=(EditText) findViewById(R.id.markernumber1);

            String mrouteName = routeName.getText().toString();
            String routedes = routedescription.getText().toString();
            String mkaddress = markeraddress.getText().toString();
            String mkdes = markerdescription.getText().toString();
            String rttag = routetag.getText().toString();
            String mknum=markernumber.getText().toString();


            DatabaseReference childRef = mRootRef.child("agentRoutes").child(mrouteName);


            DatabaseReference childRoutedes = childRef.child("Route Description");
            childRoutedes.setValue(routedes);

            DatabaseReference childcreaterId = childRef.child("Creater Id");
            childcreaterId.setValue(mUserId);

            DatabaseReference childRouteTag = childRef.child("Route Tag");
            childRouteTag.setValue(rttag);

            DatabaseReference childPoint = childRef.child("Points").child(mknum);
            DatabaseReference childMarkerAddr = childPoint.child("Address");
            childMarkerAddr.setValue(mkaddress);

            DatabaseReference childmkdes = childPoint.child("Point Description");
            childmkdes.setValue(mkdes);

        //After submit changes turn to the main page with map


        }
    }


    public void finishRoute(View v){
        int n=markerList.size();
        double sumLat=0;
        double sumLng=0;
        for(int i=0;i<n;i++){
            double lat=markerList.get(i).latitude;
            double lng=markerList.get(i).longitude;
            sumLat+=lat;
            sumLng+=lng;

        }
        LatLng center = new LatLng(sumLat/n, sumLng/n);
        CameraUpdate mapupdate = CameraUpdateFactory.newLatLngZoom(center, 12);
        mMap.moveCamera(mapupdate);








    }


}




