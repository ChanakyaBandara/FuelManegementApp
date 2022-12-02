package com.example.fuelmanegementapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Stock;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationStock extends AppCompatActivity implements httpDataManager {

    private TextView txtPetrolStock;
    private TextView txtDieselStock;
    private TextView txtSuperPetrolStock;
    private TextView txtSuperDieselStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_stock);
        txtPetrolStock = findViewById(R.id.txtPetrolStock);
        txtDieselStock = findViewById(R.id.txtDieselStock);
        txtSuperPetrolStock = findViewById(R.id.txtSuperPetrolStock);
        txtSuperDieselStock = findViewById(R.id.txtSuperDieselStock);
        loadStocks();
    }

    private void loadStocks() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.GET_STOCK_SID);
        param.put("SID", String.valueOf(FuelStationDash.fuelStation.getSid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationStock.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            if (type.equals(Constants.GET_STOCK_SID)) {
                try {
                    JSONArray jsonArray = new JSONArray(retrievedData.get());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        FuelType fuelType = new FuelType(jsonObj.getInt("fid"), jsonObj.getString("fuel"));
                        Stock stock = new Stock(jsonObj.getInt("stid"), FuelStationDash.fuelStation, fuelType, jsonObj.getInt("available_amount"));

                        if (stock.getFuelType().getFid() == 1) {
                            txtPetrolStock.setText(stock.getAvailable_amount() + " l");
                        }
                        if (stock.getFuelType().getFid() == 2) {
                            txtSuperPetrolStock.setText(stock.getAvailable_amount() + " l");
                        }
                        if (stock.getFuelType().getFid() == 3) {
                            txtDieselStock.setText(stock.getAvailable_amount() + " l");
                        }
                        if (stock.getFuelType().getFid() == 4) {
                            txtSuperDieselStock.setText(stock.getAvailable_amount() + " l");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}