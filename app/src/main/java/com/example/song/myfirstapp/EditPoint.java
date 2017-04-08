package com.example.song.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class EditPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_point);

    }

    public void buttonUpdate(View v) {
        Button button = (Button) v;
        //update in firebase

        //
        Intent intent1 = new Intent(this,EditRoute.class);
        this.startActivity(intent1);

    }

}
