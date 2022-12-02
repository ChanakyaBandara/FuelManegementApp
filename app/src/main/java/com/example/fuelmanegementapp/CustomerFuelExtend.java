package com.example.fuelmanegementapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerFuelExtend extends AppCompatActivity implements httpDataManager {

    private TextView txtExtendAmount, txtExtendRef;
    private ArrayList<Vehicle> vehicleList;
    private AppCompatSpinner spinnerType;
    private Vehicle selectedVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_fuel_extend);
        txtExtendAmount = findViewById(R.id.txtExtendAmount);
        txtExtendRef = findViewById(R.id.txtExtendRef);

        spinnerType = findViewById(R.id.selectVehicle);
        spinnerType.setPrompt("Select Vehicle");

        loadVehicles();

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVehicle = vehicleList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (vehicleList != null && vehicleList.size() > 0) {
                    selectedVehicle = vehicleList.get(0);
                }
            }
        });
    }

    private void loadVehicles() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_VEHICLES);
        param.put("cid", String.valueOf(CustomerDash.customer.getCid()));

        vehicleList = new ArrayList<Vehicle>();

        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerFuelExtend.this);
        backgroundworker.execute(param);
    }

    public void requestExtend(View view) {
        String amount = txtExtendAmount.getText().toString();
        String ref = txtExtendRef.getText().toString();

        if (!(TextUtils.isEmpty(amount) && TextUtils.isEmpty(ref)) && selectedVehicle != null) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", Constants.ADD_EXTEND);
            param.put("vid", String.valueOf(selectedVehicle.getVid()));
            param.put("amount", amount.trim());
            param.put("ref", ref.trim());
            BackgroundWorker backgroundworker = new BackgroundWorker(CustomerFuelExtend.this);
            backgroundworker.execute(param);

        } else {
            Toast.makeText(this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        try {
            if (retrievedData.isPresent()) {
                if (type.equals(Constants.LOAD_VEHICLES)) {
                    Vehicle[] vehicles = new Gson().fromJson(retrievedData.get(), Vehicle[].class);
                    for (Vehicle vehicle : vehicles) {
                        vehicleList.add(vehicle);
                    }
                    if (vehicleList.isEmpty()) {
                        Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
                    }
                    loadSpinner();
                } else if (type.equals(Constants.ADD_EXTEND)) {
                    JSONObject jsonObj = new JSONObject(retrievedData.get());
                    String status = jsonObj.getString("Status");
                    if (status.equals("1")) {
                        Toast.makeText(this, "Extend Requested!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }

    private void loadSpinner() {
        // Spinner Drop down elements
        // Creating adapter for spinner
        List<String> dataList = vehicleList.stream().map(vehicle -> vehicle.getReg_no()).collect(Collectors.toList());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataList);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerType.setAdapter(dataAdapter);
    }
}