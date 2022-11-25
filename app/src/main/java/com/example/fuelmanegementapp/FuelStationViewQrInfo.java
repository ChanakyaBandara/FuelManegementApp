package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationViewQrInfo extends AppCompatActivity implements httpDataManager {

    private TextView txtStaQRVehReg, txtStaQRVehBrand, txtStaQRVehModal, txtStaQRVehEngine, txtStaQRVehChassis, txtStaQRtotRemaining;
    private EditText txtStaPumpedAmount;
    private Vehicle vehicle;

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
        txtStaQRtotRemaining = (TextView) findViewById(R.id.txtStaQRtotRemaining);
        txtStaPumpedAmount = (EditText) findViewById(R.id.txtStaPumpedAmount);

        loadVehicleByQR(extra_qr);
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

        if (!TextUtils.isEmpty(pumpedAmount)) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "add_fuel_record");
            param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));
            param.put("vid", String.valueOf(vehicle.getVid()));
            param.put("amount", pumpedAmount.trim());
            BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewQrInfo.this);
            backgroundworker.execute(param);

        } else {
            Toast.makeText(this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            try {
                if (type == "load_vehicle_by_qr") {
                    vehicle = new Gson().fromJson(retrievedData.get(), Vehicle.class);

                    txtStaQRVehReg.setText(vehicle.getReg_no());
                    txtStaQRVehBrand.setText(vehicle.getBrand());
                    txtStaQRVehModal.setText(vehicle.getModel());
                    txtStaQRVehEngine.setText(vehicle.getEngine_no());
                    txtStaQRVehChassis.setText(vehicle.getChassis_no());

                    loadVehicleStock();
                } else if (type == "remaining_quota") {
                    JSONObject jsonObj = new JSONObject(retrievedData.get());
                    int allowed_quota = jsonObj.getInt("allowed_quota");
                    int total_amount = jsonObj.getInt("total_amount");
                    txtStaQRtotRemaining.setText(String.valueOf(allowed_quota - total_amount)+" liters");
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