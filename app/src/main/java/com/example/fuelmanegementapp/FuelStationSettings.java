package com.example.fuelmanegementapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FuelStationSettings extends AppCompatActivity implements httpDataManager {


    private AppCompatSpinner spinnerOpnCls, spinnerQueue;
    List<String> dataOpnClsList = new ArrayList<>();
    List<String> dataQueueList = new ArrayList<>();
    private static final String OPEN = "Open";
    private static final String CLOSE = "Close";
    private static final String LONG = "Long";
    private static final String MEDIUM = "Medium";
    private static final String SHORT = "Short";
    private FuelStation fuelStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_settings);
        spinnerOpnCls = findViewById(R.id.selectOpnCls);
        spinnerQueue = findViewById(R.id.selectQueue);
        loadOpnClsSpinner();
        loadQueueSpinner();
        loadStationData();

        spinnerOpnCls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerQueue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateStatus() {
        String opn_cls_status = dataOpnClsList.get(spinnerOpnCls.getSelectedItemPosition());
        String queue_status = dataQueueList.get(spinnerQueue.getSelectedItemPosition());
        if (!opn_cls_status.equals(fuelStation.getOpn_cls_status()) || !queue_status.equals(fuelStation.getQueue_status())) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", Constants.UPDATE_STATION_STATUS);
            param.put("LID", String.valueOf(FuelStationDash.fuelStation.getLid()));
            param.put("opn_cls_status", opn_cls_status);
            param.put("queue_status", queue_status);
            BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationSettings.this);
            backgroundworker.execute(param);
        }
    }

    private void loadStationData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_STATION_DATA);
        param.put("LID", String.valueOf(FuelStationDash.fuelStation.getLid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationSettings.this);
        backgroundworker.execute(param);
    }

    private void loadOpnClsSpinner() {
        // Spinner Drop down elements
        // Creating adapter for spinner
        dataOpnClsList.add(OPEN);
        dataOpnClsList.add(CLOSE);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataOpnClsList);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerOpnCls.setAdapter(dataAdapter);
    }

    private void loadQueueSpinner() {
        // Spinner Drop down elements
        // Creating adapter for spinner
        dataQueueList.add(LONG);
        dataQueueList.add(MEDIUM);
        dataQueueList.add(SHORT);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataQueueList);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerQueue.setAdapter(dataAdapter);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            if (type.equals(Constants.LOAD_STATION_DATA)) {
                fuelStation = new Gson().fromJson(retrievedData.get(), FuelStation.class);
                updateSpinners();
            } else if (type.equals(Constants.UPDATE_STATION_STATUS)) {
                String opn_cls_status = dataOpnClsList.get(spinnerOpnCls.getSelectedItemPosition());
                String queue_status = dataQueueList.get(spinnerQueue.getSelectedItemPosition());
                fuelStation.setOpn_cls_status(opn_cls_status);
                fuelStation.setQueue_status(queue_status);
                Toast.makeText(FuelStationSettings.this, "Status Updated !", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void updateSpinners() {
        String opn_cls_status = fuelStation.getOpn_cls_status();
        String queue_status = fuelStation.getQueue_status();
        if (opn_cls_status != null && !opn_cls_status.isEmpty()) {
            if (dataOpnClsList.contains(opn_cls_status.trim())) {
                int index = getIndexFromArray(dataOpnClsList, opn_cls_status);
                spinnerOpnCls.setSelection(index);
            }
        }
        if (queue_status != null && !queue_status.isEmpty()) {
            if (dataQueueList.contains(queue_status.trim())) {
                int index = getIndexFromArray(dataQueueList, queue_status);
                spinnerQueue.setSelection(index);
            }
        }

    }

    private int getIndexFromArray(List<String> arr, String value) {
        int index = -1;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(value.trim())) {
                index = i;
                break;
            }
        }
        return index;
    }
}