package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

public class FuelStationArival extends AppCompatActivity implements httpDataManager {

    private EditText txtStationArrDate, txtStationArrAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_arival);

        txtStationArrDate = (EditText) findViewById(R.id.txtStationArrDate);
        txtStationArrAmount = (EditText) findViewById(R.id.txtStationArrAmount);
    }

    public void addArrival(View view) {
        String date = txtStationArrDate.getText().toString();
        String amount = txtStationArrAmount.getText().toString();
        if (!(TextUtils.isEmpty(date) && TextUtils.isEmpty(amount))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "add_fuel_arrival");
            param.put("amount", amount);
            param.put("timestamp", date);
            param.put("ft_id", "1");
            param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));
            Backgroundworker backgroundworker = new Backgroundworker(FuelStationArival.this);
            backgroundworker.execute(param);
        } else {
            Toast.makeText(FuelStationArival.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        try {
            if (retrievedData.isPresent()){
                if (type.equals("add_fuel_arrival")){
                    JSONObject jsonObj = new JSONObject(retrievedData.get());
                    String status = jsonObj.getString("Status");
                    if (status.equals("1")) {
                        Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }
}