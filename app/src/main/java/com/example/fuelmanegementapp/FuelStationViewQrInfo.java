package com.example.fuelmanegementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class FuelStationViewQrInfo extends AppCompatActivity implements httpDataManager {

    private TextView txtStaQRVehReg, txtStaQRVehBrand, txtStaQRVehModal, txtStaQRVehEngine, txtStaQRVehChassis, txtStaQRtotRemaining, txtStaQRQuota, txtStaQRExtend;
    private EditText txtStaPumpedAmount;
    private AppCompatSpinner fuelSpinner;
    private Vehicle vehicle;
    private ArrayList<String> fuelList;
    private ArrayList<String> fuelListKeys;
    private int remainingQuota = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_view_qr_info);
        String extra_qr = getIntent().getStringExtra("Extra_qr");

        txtStaQRVehReg = findViewById(R.id.txtStaQRVehReg);
        txtStaQRVehBrand = findViewById(R.id.txtStaQRVehBrand);
        txtStaQRVehModal = findViewById(R.id.txtStaQRVehModal);
        txtStaQRVehEngine = findViewById(R.id.txtStaQRVehEngine);
        txtStaQRVehChassis = findViewById(R.id.txtStaQRVehChassis);
        txtStaQRQuota = findViewById(R.id.txtStaQRQuota);
        txtStaQRExtend = findViewById(R.id.txtStaQRExtend);
        txtStaQRtotRemaining = findViewById(R.id.txtStaQRtotRemaining);
        txtStaPumpedAmount = findViewById(R.id.txtStaPumpedAmount);

        fuelSpinner = findViewById(R.id.fuelDrop);
        fuelSpinner.setPrompt("Choose Fuel Type");

        fuelList = new ArrayList<String>();
        fuelListKeys = new ArrayList<String>();

        loadFuelSpinnerData();
        loadVehicleByQR(extra_qr);
    }

    private void loadFuelSpinnerData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_FUEL_TYPES);
        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
        backgroundworker.execute(param);
    }

    private void loadVehicleByQR(String extra_qr) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_VEHICLE_BY_QR);
        param.put("qr", extra_qr);

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
        backgroundworker.execute(param);
    }

    public void update(View view) {
        String pumpedAmount = txtStaPumpedAmount.getText().toString();
        if (Integer.parseInt(pumpedAmount) != 0 && remainingQuota >= Integer.parseInt(pumpedAmount)) {
            if (!TextUtils.isEmpty(pumpedAmount)) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("type", Constants.ADD_FUEL_RECORD);
                param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));
                param.put("vid", String.valueOf(vehicle.getVid()));
                param.put("fid", fuelListKeys.get(fuelSpinner.getSelectedItemPosition()));
                param.put("amount", pumpedAmount.trim());
                BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
                backgroundworker.execute(param);

            } else {
                Toast.makeText(this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter allowed amount!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            try {
                if (type.equals(Constants.LOAD_VEHICLE_BY_QR)) {
                    vehicle = new Gson().fromJson(retrievedData.get(), Vehicle.class);

                    txtStaQRVehReg.setText(vehicle.getReg_no());
                    txtStaQRVehBrand.setText(vehicle.getBrand());
                    txtStaQRVehModal.setText(vehicle.getModel());
                    txtStaQRVehEngine.setText(vehicle.getEngine_no());
                    txtStaQRVehChassis.setText(vehicle.getChassis_no());

                    loadVehicleStock();
                } else if (type.equals(Constants.REMAINING_QUOTA)) {
                    JSONObject jsonObj = new JSONObject(retrievedData.get());
                    int allowed_quota = jsonObj.getInt("allowed_quota");
                    int extend_amount = jsonObj.getInt("extend_amount");
                    int total_amount = jsonObj.getInt("total_amount");
                    remainingQuota = (allowed_quota + extend_amount) - total_amount;
                    txtStaQRtotRemaining.setText(remainingQuota + " liters");
                    txtStaQRQuota.setText(allowed_quota + " liters");
                    txtStaQRExtend.setText(extend_amount + " liters");
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

                } else if (type.equals(Constants.ADD_FUEL_RECORD)) {
                    Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, FuelStationRecords.class);
                    this.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadVehicleStock() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.REMAINING_QUOTA);
        param.put("vid", String.valueOf(vehicle.getVid()));

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
        backgroundworker.execute(param);
    }
}