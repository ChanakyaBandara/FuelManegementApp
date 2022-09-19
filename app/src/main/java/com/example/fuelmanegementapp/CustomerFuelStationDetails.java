package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.fuelmanegementapp.models.FuelStation;

public class CustomerFuelStationDetails extends AppCompatActivity {

    private FuelStation fuelStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_fuel_station_details);

        if(getIntent().getExtras() != null) {
            fuelStation = (FuelStation) getIntent().getSerializableExtra("FuelStationObj");
            populateStationData();
        }
    }

    private void populateStationData() {
        Toast.makeText(this, fuelStation.getName(), Toast.LENGTH_SHORT).show();
    }
}