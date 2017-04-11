package com.example.song.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EditPoint extends AppCompatActivity {
    private String routeName;
    private String pointNumber;
    private String routeid;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
//    ArrayList<DataSnapshot> qroutes=new ArrayList<>();
//    ArrayList<String> p = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_point);


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







    public void buttonUpdate(View v) {
        //update in firebase
        TextView newAddress=(TextView) findViewById(R.id.newmarkeraddr);
        String naddr=newAddress.getText().toString();
        TextView newDesc=(TextView) findViewById(R.id.newpointdesc);
        String ndesc=newDesc.getText().toString();

        mDatabase.child("Points").child(routeid).child("Address").setValue(naddr);
        mDatabase.child("Points").child(routeid).child("Point Description").setValue(ndesc);
        Intent intent1 = new Intent(this,EditRoute.class);
        this.startActivity(intent1);

    }

}
