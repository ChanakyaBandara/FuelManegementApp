package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Customer;
import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.gson.Gson;

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
    public void retrieveData(String type,Optional<String> retrievedData) {
        if(retrievedData.isPresent()){
            Log.i("Error_retrievedData", retrievedData.get());
            customer = new Gson().fromJson(retrievedData.get(), Customer.class);
        }
    }

    public void goToStations(View view) {
        Intent intent = new Intent(this, CustomerViewFuelStations.class);
        this.startActivity(intent);
    }

    public void goToVehicles(View view) {
        Intent intent = new Intent(this, CustomerViewVehicles.class);
        this.startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(this, CustomerProfile.class);
        this.startActivity(intent);
    }

    public void goToRecords(View view) {
        Intent intent = new Intent(this, CustomerRecords.class);
        this.startActivity(intent);
    }

    public void goToSettings(View view) {
    }

    public void goToSpecialQr(View view) {
    }

    public void goToExtends(View view) {
    }
}