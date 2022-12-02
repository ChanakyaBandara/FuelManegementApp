package com.example.fuelmanegementapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Handler handler = new Handler();        //handler object
        Runnable runnable = new Runnable() {        //running thread
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Login.class);     //redirects to next page
                startActivity(intent);
                Splash.this.finish();

            }
        };
        handler.postDelayed(runnable, 3000);        //running thread 3000 milis
    }
}