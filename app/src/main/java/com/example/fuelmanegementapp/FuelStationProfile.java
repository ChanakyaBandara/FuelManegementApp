package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationProfile extends AppCompatActivity implements httpDataManager {

    private TextView txtName,txtEmail,txtRegNo,txtPhone,txtAddress,txtCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_profile);

        txtName = (TextView) findViewById(R.id.txtStationProName);
        txtEmail = (TextView) findViewById(R.id.txtStationProEmail);
        txtRegNo = (TextView) findViewById(R.id.txtStationProRegNo);
        txtPhone = (TextView) findViewById(R.id.txtStationProPhone);
        txtAddress = (TextView) findViewById(R.id.txtStationProAddress);
        txtCity = (TextView) findViewById(R.id.txtStationProCity);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_station_data");
        param.put("LID", String.valueOf(FuelStationDash.fuelStation.getLid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationProfile.this);
        backgroundworker.execute(param);
    }

    public void goBack(View view) {
        Intent intent = new Intent(FuelStationProfile.this, CustomerDash.class);
        startActivity(intent);
        finish();
    }

    public void logoutFuel(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(Login.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(FuelStationProfile.this, Login.class);
        FuelStationDash.fuelStation = null;
        FuelStationDash.LID = null;
        startActivity(intent);
        finish();
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()){
            Log.i("Error_Check",retrievedData.get());

            FuelStation fuelStation = new Gson().fromJson(retrievedData.get(), FuelStation.class);

            txtName.setText(fuelStation.getName());
            txtEmail.setText(fuelStation.getEmail());
            txtPhone.setText(fuelStation.getPhone());
            txtRegNo.setText(fuelStation.getReg_no());
            txtAddress.setText(fuelStation.getAddress());
            txtCity.setText(fuelStation.getCity());
        }
    }

}