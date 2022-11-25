package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Stock;
import com.example.fuelmanegementapp.services.BackgroundWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationStock extends AppCompatActivity implements httpDataManager {

    private TextView txtPetrolStock;
    private TextView txtDieselStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_stock);
        txtPetrolStock = (TextView) findViewById(R.id.txtPetrolStock);
        txtDieselStock = (TextView) findViewById(R.id.txtDieselStock);
        loadStocks();
    }

    private void loadStocks() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "get_stock_sid");
        param.put("SID", String.valueOf(FuelStationDash.fuelStation.getSid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationStock.this);
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
                        Stock stock = new Stock(jsonObj.getInt("stid"), FuelStationDash.fuelStation, fuelType, jsonObj.getInt("available_amount"));

                        if (stock.getFuelType().getFid() == 1) {
                            txtPetrolStock.setText(stock.getAvailable_amount()+" l");
                        } else {
                            txtDieselStock.setText(stock.getAvailable_amount()+" l");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}