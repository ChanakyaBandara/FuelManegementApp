package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Customer;
import com.example.fuelmanegementapp.services.Backgroundworker;

import java.util.HashMap;
import java.util.Optional;

public class CustomerDash extends AppCompatActivity implements httpDataManager {

    public static String LID;
    public static Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (TextUtils.isEmpty(LID)) {
            LID = getIntent().getStringExtra("LID");
        }

        load_member_name(LID);
    }

    private void load_member_name(String user) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_customer_data");
        param.put("LID", user);
        Backgroundworker backgroundworker = new Backgroundworker(CustomerDash.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(Optional<String> retrievedData) {
        if(retrievedData.isPresent()){
            Log.i("Error_retrievedData", retrievedData.get());
        }
    }
}