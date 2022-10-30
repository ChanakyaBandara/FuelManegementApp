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
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationDash extends AppCompatActivity implements httpDataManager {

    public static String LID;
    public static FuelStation fuelStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_dash);

        if (TextUtils.isEmpty(LID)) {
            LID = getIntent().getStringExtra("LID");
        }

        load_member_name(LID);
    }

    private void load_member_name(String lid) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_station_data");
        param.put("LID", lid);
        Backgroundworker backgroundworker = new Backgroundworker(FuelStationDash.this, this.getApplicationContext());
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if(retrievedData.isPresent()){
            Log.i("Error_retrievedData", retrievedData.get());
            fuelStation = new Gson().fromJson(retrievedData.get(), FuelStation.class);
        }
    }

    public void goToScanQr(View view) {
        Intent intent = new Intent(this, FuelStationScanQr.class);
        this.startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(this, FuelStationProfile.class);
        this.startActivity(intent);
    }

    public void goToStocks(View view) {
    }

    public void goToRecords(View view) {
    }

    public void goToArrival(View view) {
    }

    public void goToSettings(View view) {
    }
}