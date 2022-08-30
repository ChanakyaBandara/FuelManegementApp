package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Customer;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.recycleviews.RecycleViewConfig;
import com.example.fuelmanegementapp.recycleviews.vehicle.VehicleAdapter;
import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerViewVehicles extends AppCompatActivity implements httpDataManager {

    private List<Vehicle> vehicleList;
    private List<String> vehicleIdList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_vehicles);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_vehicles");
        param.put("cid", String.valueOf(CustomerDash.customer.getCid()));

        vehicleList = new ArrayList<Vehicle>();
        vehicleIdList = new ArrayList<String>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Backgroundworker backgroundworker = new Backgroundworker(CustomerViewVehicles.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type,Optional<String> retrievedData) {
        vehicleList.clear();
        vehicleIdList.clear();

        if (retrievedData.isPresent()){

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
    }
}