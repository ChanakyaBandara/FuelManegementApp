package com.example.fuelmanegementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.VehicleType;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

public class CustomerAddVehicle extends AppCompatActivity implements httpDataManager {

    private EditText txtInCusVehReg, txtInCusVehBrand, txtInCusVehModal, txtInCusVehEngine, txtInCusVehChassis;
    private AppCompatSpinner fuelSpinner;
    private AppCompatSpinner vehicleTypeSpinner;
    private String fuelSelectedCategory = "1";
    private ArrayList<String> fuelList;
    private ArrayList<String> fuelListKeys;
    private String vehicleTypeSelectedCategory = "1";
    private ArrayList<String> vehicleTypeList;
    private ArrayList<String> vehicleTypeListKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_vehicle);

        txtInCusVehReg = findViewById(R.id.txtInCusVehReg);
        txtInCusVehBrand = findViewById(R.id.txtInCusVehBrand);
        txtInCusVehModal = findViewById(R.id.txtInCusVehModal);
        txtInCusVehEngine = findViewById(R.id.txtInCusVehEngine);
        txtInCusVehChassis = findViewById(R.id.txtInCusVehChassis);

        fuelSpinner = findViewById(R.id.fuelDrop);
        fuelSpinner.setPrompt("Choose Fuel Type");

        vehicleTypeSpinner = findViewById(R.id.vehicleTypeDrop);
        vehicleTypeSpinner.setPrompt("Choose Vehicle Type");

        fuelList = new ArrayList<String>();
        fuelListKeys = new ArrayList<String>();

        vehicleTypeList = new ArrayList<String>();
        vehicleTypeListKeys = new ArrayList<String>();

        fuelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fuelSelectedCategory = fuelListKeys.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fuelSelectedCategory = "1";
            }
        });

        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleTypeSelectedCategory = vehicleTypeListKeys.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicleTypeSelectedCategory = "1";
            }
        });

        loadFuelSpinnerData();
        loadVehicleTypeSpinnerData();
    }

    public void registerVehicle(View view) {
        String regNo = txtInCusVehReg.getText().toString();
        String brand = txtInCusVehBrand.getText().toString();
        String modal = txtInCusVehModal.getText().toString();
        String engine = txtInCusVehEngine.getText().toString();
        String chassis = txtInCusVehChassis.getText().toString();
        if (!(TextUtils.isEmpty(regNo) && TextUtils.isEmpty(brand) && TextUtils.isEmpty(modal) && TextUtils.isEmpty(engine) && TextUtils.isEmpty(chassis))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", Constants.ADD_VEHICLE);
            param.put("regNo", regNo);
            param.put("brand", brand);
            param.put("modal", modal);
            param.put("engine", engine);
            param.put("chassis", chassis);
            param.put("qr", "VEH#" + regNo + Calendar.getInstance().getTimeInMillis());
            param.put("vtid", vehicleTypeSelectedCategory);
            param.put("cid", String.valueOf(CustomerDash.customer.getCid()));
            param.put("fid", fuelSelectedCategory);
            BackgroundWorker backgroundworker = new BackgroundWorker(CustomerAddVehicle.this);
            backgroundworker.execute(param);
        } else {
            Toast.makeText(CustomerAddVehicle.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFuelSpinnerData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_FUEL_TYPES);
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerAddVehicle.this);
        backgroundworker.execute(param);
    }

    private void loadVehicleTypeSpinnerData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_VEHICLE_TYPES);
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerAddVehicle.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            if (type.equals(Constants.ADD_VEHICLE)) {

                Toast.makeText(this, Constants.SUCCESSFULLY_ADDED_MSG, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CustomerViewVehicles.class);
                this.startActivity(intent);
                finish();

            } else if (type.equals(Constants.LOAD_FUEL_TYPES)) {
                FuelType[] fuelTypes = new Gson().fromJson(retrievedData.get(), FuelType[].class);

                for (FuelType fuelType : fuelTypes) {
                    fuelList.add(fuelType.getFuel());
                    fuelListKeys.add(String.valueOf(fuelType.getFid()));
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, fuelList);

                // Drop down layout style - list view with radio button
                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                fuelSpinner.setAdapter(dataAdapter);

            } else if (type.equals(Constants.LOAD_VEHICLE_TYPES)) {
                VehicleType[] vehicleTypes = new Gson().fromJson(retrievedData.get(), VehicleType[].class);

                for (VehicleType vehicleType : vehicleTypes) {
                    vehicleTypeList.add(vehicleType.getType());
                    vehicleTypeListKeys.add(String.valueOf(vehicleType.getVtid()));
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, vehicleTypeList);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                vehicleTypeSpinner.setAdapter(dataAdapter);
            }
        }

    }
}