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

        txtStaQRVehReg = (TextView) findViewById(R.id.txtStaQRVehReg);
        txtStaQRVehBrand = (TextView) findViewById(R.id.txtStaQRVehBrand);
        txtStaQRVehModal = (TextView) findViewById(R.id.txtStaQRVehModal);
        txtStaQRVehEngine = (TextView) findViewById(R.id.txtStaQRVehEngine);
        txtStaQRVehChassis = (TextView) findViewById(R.id.txtStaQRVehChassis);
        txtStaQRQuota = (TextView) findViewById(R.id.txtStaQRQuota);
        txtStaQRExtend = (TextView) findViewById(R.id.txtStaQRExtend);
        txtStaQRtotRemaining = (TextView) findViewById(R.id.txtStaQRtotRemaining);
        txtStaPumpedAmount = (EditText) findViewById(R.id.txtStaPumpedAmount);

        fuelSpinner = (AppCompatSpinner) findViewById(R.id.fuelDrop);
        fuelSpinner.setPrompt("Choose Fuel Type");

        fuelList = new ArrayList<String>();
        fuelListKeys = new ArrayList<String>();

        loadFuelSpinnerData();
        loadVehicleByQR(extra_qr);
    }

    private void loadFuelSpinnerData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_fuel_types");
        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
        backgroundworker.execute(param);
    }

    private void loadVehicleByQR(String extra_qr) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_vehicle_by_qr");
        param.put("qr", extra_qr);

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
        backgroundworker.execute(param);
    }

    public void update(View view) {
        String pumpedAmount = txtStaPumpedAmount.getText().toString();
        if (Integer.parseInt(pumpedAmount) != 0 && remainingQuota >= Integer.parseInt(pumpedAmount)) {
            if (!TextUtils.isEmpty(pumpedAmount)) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("type", "add_fuel_record");
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
                if (type.equals("load_vehicle_by_qr")) {
                    vehicle = new Gson().fromJson(retrievedData.get(), Vehicle.class);

                    txtStaQRVehReg.setText(vehicle.getReg_no());
                    txtStaQRVehBrand.setText(vehicle.getBrand());
                    txtStaQRVehModal.setText(vehicle.getModel());
                    txtStaQRVehEngine.setText(vehicle.getEngine_no());
                    txtStaQRVehChassis.setText(vehicle.getChassis_no());

                    loadVehicleStock();
                } else if (type.equals("remaining_quota")) {
                    JSONObject jsonObj = new JSONObject(retrievedData.get());
                    int allowed_quota = jsonObj.getInt("allowed_quota");
                    int extend_amount = jsonObj.getInt("extend_amount");
                    int total_amount = jsonObj.getInt("total_amount");
                    remainingQuota = (allowed_quota + extend_amount) - total_amount;
                    txtStaQRtotRemaining.setText(remainingQuota + " liters");
                    txtStaQRQuota.setText(allowed_quota + " liters");
                    txtStaQRExtend.setText(extend_amount + " liters");
                } else if (type.equals("load_fuel_types")) {
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

                } else if (type.equals("add_fuel_record")) {
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
        param.put("type", "remaining_quota");
        param.put("vid", String.valueOf(vehicle.getVid()));

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
        backgroundworker.execute(param);
    }
}