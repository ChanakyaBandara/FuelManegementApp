package com.example.fuelmanegementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.recycleviews.RecycleViewConfig;
import com.example.fuelmanegementapp.recycleviews.customer.vehicle.VehicleAdapter;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomerViewVehicles extends AppCompatActivity implements httpDataManager {

    private List<Vehicle> vehicleList;
    private List<String> vehicleIdList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_vehicles);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_VEHICLES);
        param.put("cid", String.valueOf(CustomerDash.customer.getCid()));

        vehicleList = new ArrayList<Vehicle>();
        vehicleIdList = new ArrayList<String>();
        recyclerView = findViewById(R.id.recyclerView);

        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerViewVehicles.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        vehicleList.clear();
        vehicleIdList.clear();

        if (retrievedData.isPresent()) {
            Vehicle[] vehicles = new Gson().fromJson(retrievedData.get(), Vehicle[].class);
            for (Vehicle vehicle : vehicles) {
                vehicleList.add(vehicle);
                vehicleIdList.add(String.valueOf(vehicle.getVid()));
            }
        }
        if (vehicleList.isEmpty()) {
            Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
        } else {
            VehicleAdapter vehicleAdapter = new VehicleAdapter(vehicleList, vehicleIdList, this);
            new RecycleViewConfig().setConfig(recyclerView, this, vehicleAdapter);
        }
    }

    public void goToAddVehicle(View view) {
        Intent intent = new Intent(this, CustomerAddVehicle.class);
        this.startActivity(intent);
        finish();
    }
}