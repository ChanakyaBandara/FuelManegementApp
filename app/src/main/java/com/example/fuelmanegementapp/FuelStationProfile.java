package com.example.fuelmanegementapp;

import static com.example.fuelmanegementapp.Login.nic;
import static com.example.fuelmanegementapp.Login.pass;
import static com.example.fuelmanegementapp.Login.sts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationProfile extends AppCompatActivity implements httpDataManager {

    private TextView txtName, txtEmail, txtRegNo, txtPhone, txtAddress, txtCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_profile);

        txtName = findViewById(R.id.txtStationProName);
        txtEmail = findViewById(R.id.txtStationProEmail);
        txtRegNo = findViewById(R.id.txtStationProRegNo);
        txtPhone = findViewById(R.id.txtStationProPhone);
        txtAddress = findViewById(R.id.txtStationProAddress);
        txtCity = findViewById(R.id.txtStationProCity);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_STATION_DATA);
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
        editor.remove(sts);
        editor.remove(nic);
        editor.remove(pass);
        editor.apply();
        Intent intent = new Intent(FuelStationProfile.this, Login.class);
        FuelStationDash.fuelStation = null;
        FuelStationDash.LID = null;
        startActivity(intent);
        finish();
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
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