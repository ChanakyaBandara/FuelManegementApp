package com.example.fuelmanegementapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Record;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.recycleviews.RecycleViewConfig;
import com.example.fuelmanegementapp.recycleviews.station.record.StationRecordAdapter;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FuelStationRecords extends AppCompatActivity implements httpDataManager {

    private List<Record> recordList;
    private List<String> recordIdList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_records);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_STATION_RECORDS);
        param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));

        recordList = new ArrayList<Record>();
        recordIdList = new ArrayList<String>();
        recyclerView = findViewById(R.id.stationRecordRecyclerView);

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationRecords.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        recordList.clear();
        recordIdList.clear();

        if (retrievedData.isPresent()) {

            List<Record> records = getRecords(retrievedData);
            for (Record record : records) {
                recordList.add(record);
                recordIdList.add(String.valueOf(record.getRid()));
            }

        }
        if (recordList.isEmpty()) {
            Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
        } else {
            StationRecordAdapter recordAdapter = new StationRecordAdapter(recordList, recordIdList, this);
            new RecycleViewConfig().setConfig(recyclerView, this, recordAdapter);
        }
    }

    private List<Record> getRecords(Optional<String> retrievedData) {
        List<Record> records = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(retrievedData.get());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                FuelType fuelType = new FuelType(jsonObj.getString("fuel"));
                Vehicle vehicle = new Vehicle();
                vehicle.setVid(jsonObj.getInt("vid"));
                vehicle.setReg_no(jsonObj.getString("reg_no"));
                vehicle.setFuelType(fuelType);
                FuelStation fuelStation = new FuelStation();
                fuelStation.setSid(jsonObj.getInt("sid"));
                fuelStation.setName(jsonObj.getString("name"));
                fuelStation.setAddress(jsonObj.getString("address"));
                Record record = new Record(jsonObj.getInt("rid"), jsonObj.getString("timestamp"), vehicle, fuelStation, jsonObj.getInt("amount"), jsonObj.getInt("status"));
                records.add(record);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return records;
    }

    public void updateAsCancelled(Record record) {

    }
}