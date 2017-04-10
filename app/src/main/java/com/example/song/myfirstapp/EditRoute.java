package com.example.song.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class EditRoute extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String s = extras.getString("CurrentRoute");
//            String value = extras.getString("Current Route");
            //The key argument here must match that used in the other activity
        String s = getIntent().getStringExtra("CurrentRoute");

            TextView RouteName=(TextView) findViewById(R.id.RName);
            RouteName.setText(s);
//        }

    }

    public void buttonEdit(View v) {
        //Button button = (Button) v;
        Intent intent1 = new Intent(this,EditPoint.class);
        this.startActivity(intent1);

    }

}
