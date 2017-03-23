package com.example.song.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Button;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.widget.ViewFlipper;
import android.view.MenuItem;
import android.widget.Button;

public class profilename extends AppCompatActivity {
    private TextView mTextMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilename);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        vf.setDisplayedChild(0);
//        Button button = (Button)findViewById(R.id.edit_profile);
//        button.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//
//            }
//        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
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

                    return true;
            }
            return false;
        }

    };
    public void buttonEditprofileClick(View v){
        Button button = (Button) v;
        Intent intent1 = new Intent(this,RouteView.class);
        this.startActivity(intent1);
    }

    public void buttonAddRouteClick(View v){
        Button button = (Button) v;
        Intent intent1 = new Intent(this,RouteView.class);
        this.startActivity(intent1);
    }

    public void buttonEditRouteClick(View v){
        Button button = (Button) v;
        Intent intent1 = new Intent(this,RouteView.class);
        this.startActivity(intent1);
    }



    public void gotoEditProfile(View v) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent2 = new Intent(this, Change_Profile.class);
        startActivity(intent2);
    }

}


