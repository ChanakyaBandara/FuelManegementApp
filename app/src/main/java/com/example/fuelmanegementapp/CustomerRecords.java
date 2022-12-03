package com.example.fuelmanegementapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Record;
import com.example.fuelmanegementapp.models.Vehicle;
import com.example.fuelmanegementapp.recycleviews.RecycleViewConfig;
import com.example.fuelmanegementapp.recycleviews.customer.record.RecordAdapter;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomerRecords extends AppCompatActivity implements httpDataManager {

    private List<Record> recordList;
    private List<String> recordIdList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_records);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_RECORDS);
        param.put("cid", String.valueOf(CustomerDash.customer.getCid()));

        recordList = new ArrayList<Record>();
        recordIdList = new ArrayList<String>();
        recyclerView = findViewById(R.id.recordRecyclerView);

        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerRecords.this);
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
            RecordAdapter recordAdapter = new RecordAdapter(recordList, recordIdList, this);
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
            Log.i("Error_test1", e.toString());
        }

        return records;
    }
}