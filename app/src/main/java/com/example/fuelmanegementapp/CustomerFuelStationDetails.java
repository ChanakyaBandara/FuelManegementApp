package com.example.fuelmanegementapp;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Stock;
import com.example.fuelmanegementapp.services.BackgroundWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class CustomerFuelStationDetails extends AppCompatActivity implements httpDataManager {

    private FuelStation fuelStation;
    private TextView txtFSName, txtFSEmail, txtFSPhone, txtFSReg, txtFSCity, txtFSAddress, txtFSOpnCls, txtFSQueue;
    private TextView txtPetrolPercentage, txtDieselPercentage;
    ProgressBar petrolProgressBar, dieselProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_fuel_station_details);

        txtFSName = (TextView) findViewById(R.id.txtFSName);
        txtFSEmail = (TextView) findViewById(R.id.txtFSEmail);
        txtFSPhone = (TextView) findViewById(R.id.txtFSPhone);
        txtFSReg = (TextView) findViewById(R.id.txtFSReg);
//        txtFSCity = (TextView) findViewById(R.id.txtFSCity);
        txtFSAddress = (TextView) findViewById(R.id.txtFSAddress);
        txtFSOpnCls = (TextView) findViewById(R.id.txtFSOpnCls);
        txtFSQueue = (TextView) findViewById(R.id.txtFSQueue);
        txtPetrolPercentage = (TextView) findViewById(R.id.txtPetrolPercentage);
        txtDieselPercentage = (TextView) findViewById(R.id.txtDieselPercentage);
        petrolProgressBar = (ProgressBar) findViewById(R.id.petrolProgressBar);
        dieselProgressBar = (ProgressBar) findViewById(R.id.dieselProgressBar);

        if (getIntent().getExtras() != null) {
            fuelStation = (FuelStation) getIntent().getSerializableExtra("FuelStationObj");
            populateStationData();
        }
    }

    private void populateStationData() {
        txtFSName.setText(fuelStation.getName());
        txtFSEmail.setText(fuelStation.getEmail());
        txtFSPhone.setText(fuelStation.getPhone());
        txtFSReg.setText(fuelStation.getReg_no());
//        txtFSCity.setText(fuelStation.getCity());
        txtFSAddress.setText(fuelStation.getAddress());
        txtFSOpnCls.setText(fuelStation.getOpn_cls_status());
        txtFSQueue.setText(fuelStation.getQueue_status());
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "get_stock_sid");
        param.put("SID", String.valueOf(fuelStation.getSid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerFuelStationDetails.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            if (type.equals("get_stock_sid")) {
                try {
                    JSONArray jsonArray = new JSONArray(retrievedData.get());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        FuelType fuelType = new FuelType(jsonObj.getInt("fid"), jsonObj.getString("fuel"));
                        Stock stock = new Stock(jsonObj.getInt("stid"), fuelStation, fuelType, jsonObj.getInt("available_amount"));

                        if (stock.getFuelType().getFid() == 1) {
                            petrolProgressBar.setProgress(stock.getAvailable_amount() / 100);
                            txtPetrolPercentage.setText(stock.getAvailable_amount() / 100 + " %");
                        } else {
                            dieselProgressBar.setProgress(stock.getAvailable_amount() / 100);
                            txtDieselPercentage.setText(stock.getAvailable_amount() / 100 + " %");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}