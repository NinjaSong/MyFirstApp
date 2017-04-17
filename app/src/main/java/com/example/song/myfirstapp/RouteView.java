package com.example.song.myfirstapp;

import android.app.Activity;
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
import java.math.BigDecimal;


import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
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

    TextView m_response;


    PayPalConfiguration m_configuration;
    // the id is the link to the paypal account, we have to create an app and get its id
    String m_paypalClientId = "Aabj-VSAGmLsuQ8l8VO-EvMbXZIml7mofebDIgPkUq4rmYApA2ycwtuCgxC-b6sNzGflnW0RnkaII4OA";
    Intent m_service;
    int m_paypalRequestCode = 999; // or any number you want


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
        m_configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // sandbox for test, production for real
                .clientId(m_paypalClientId);

        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration); // configuration above
        startService(m_service); // paypal service, listening to calls to paypal app


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

    public void pay(View view)
    {
        PayPalPayment cart = new PayPalPayment(new BigDecimal(10), "USD", "Cart Pay",
                PayPalPayment.PAYMENT_INTENT_SALE);


        Intent intent = new Intent(this, PaymentActivity.class); // it's not paypalpayment, it's paymentactivity !
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, cart);
        startActivityForResult(intent, m_paypalRequestCode);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == m_paypalRequestCode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                // we have to confirm that the payment worked to avoid fraud
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if(confirmation != null)
                {
                    String state = confirmation.getProofOfPayment().getState();

                    if(state.equals("approved")) // if the payment worked, the state equals approved
                        m_response.setText("payment approved");
                    else
                        m_response.setText("error in the payment");
                }
                else
                    m_response.setText("confirmation is null");
            }
        }
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
