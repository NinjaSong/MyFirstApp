package com.example.song.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }
    public void FirstPageLogin(View v){
        Intent intent = new Intent(this,LogInActivity.class);
        this.startActivity(intent);
    }
    public void FirstPageRegister(View v){
        Intent intent = new Intent(this,SignUpActivity.class);
        this.startActivity(intent);
    }

}
