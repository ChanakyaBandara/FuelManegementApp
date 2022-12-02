package com.example.fuelmanegementapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelArrival;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.recycleviews.RecycleViewConfig;
import com.example.fuelmanegementapp.recycleviews.station.fuelarrival.StationFuelArrivalAdapter;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FuelStationArrival extends AppCompatActivity implements httpDataManager {


    private List<FuelArrival> fuelArrivalList;
    private List<String> fuelArrivalIdList;
    private RecyclerView recyclerView;
    private Dialog myDialog;
    private ArrayList<String> fuelList;
    private ArrayList<String> fuelListKeys;
    private AppCompatSpinner fuelSpinner;
    private DatePickerDialog picker;
    private int yr, mon, mday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_arival);


        yr = 0;
        mon = 0;
        mday = 0;
        fuelArrivalList = new ArrayList<FuelArrival>();
        fuelArrivalIdList = new ArrayList<String>();
        recyclerView = findViewById(R.id.recyclerView);
        myDialog = new Dialog(this);

        fuelList = new ArrayList<String>();
        fuelListKeys = new ArrayList<String>();

        loadFuelSpinnerData();
        loadFuelArrivals();
    }

    private void loadFuelSpinnerData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_FUEL_TYPES);
        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationArrival.this);
        backgroundworker.execute(param);
    }

    public void addArrival(View view) {
        myDialog.setContentView(R.layout.custom_popup_arival);
        Button btnPopupBtn = myDialog.findViewById(R.id.btnTxtStationArr);
        EditText txtStationArrAmount = myDialog.findViewById(R.id.txtStationArrAmount);
        EditText txtStationArrDate = myDialog.findViewById(R.id.txtStationArrDate);
        fuelSpinner = myDialog.findViewById(R.id.txtStationArrFuelDrop);

        txtStationArrDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(FuelStationArrival.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtStationArrDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                yr = year;
                                mon = (monthOfYear + 1);
                                mday = dayOfMonth;
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        updateSpinnerData();
        btnPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = txtStationArrDate.getText().toString();
                String amount = txtStationArrAmount.getText().toString();
                String ft_id = fuelListKeys.get(fuelSpinner.getSelectedItemPosition());
                if (!(TextUtils.isEmpty(date) && TextUtils.isEmpty(amount))) {
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("type", Constants.ADD_FUEL_ARRIVAL);
                    param.put("amount", amount);
                    param.put("timestamp", date);
                    param.put("ft_id", ft_id);
                    param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));
                    BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationArrival.this);
                    backgroundworker.execute(param);

                    myDialog.dismiss();
                } else {
                    Toast.makeText(FuelStationArrival.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void loadFuelArrivals() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_FUEL_ARRIVALS);
        param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationArrival.this);
        backgroundworker.execute(param);
    }

    public void updateAsArrived(FuelArrival fuelArrival) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.UPDATE_FUEL_ARRIVAL_STATUS);
        param.put("fa_id", String.valueOf(fuelArrival.getFa_id()));
        param.put("fid", String.valueOf(fuelArrival.getFuelType().getFid()));
        param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));
        param.put("amount", String.valueOf(fuelArrival.getAmount()));
        param.put("status", "2");

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationArrival.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        try {
            if (retrievedData.isPresent()) {
                Log.i("Error_Check", retrievedData.get());
                if (type.equals(Constants.LOAD_FUEL_ARRIVALS)) {
                    fuelArrivalList.clear();
                    fuelArrivalIdList.clear();
                    if (retrievedData.isPresent()) {
                        JSONArray jsonArray = new JSONArray(retrievedData.get());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FuelArrival fuelArrival = new Gson().fromJson(jsonArray.get(i).toString(), FuelArrival.class);
                            FuelType fuelType = new Gson().fromJson(jsonArray.get(i).toString(), FuelType.class);
                            fuelArrival.setFuelType(fuelType);
                            fuelArrivalList.add(fuelArrival);
                            fuelArrivalIdList.add(String.valueOf(fuelArrival.getFa_id()));
                        }
                    }
                    if (fuelArrivalList.isEmpty()) {
                        Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
                    } else {
                        StationFuelArrivalAdapter fuelArrivalAdapter = new StationFuelArrivalAdapter(fuelArrivalList, fuelArrivalIdList, this);
                        new RecycleViewConfig().setConfig(recyclerView, this, fuelArrivalAdapter);
                    }
                } else if (type.equals(Constants.ADD_FUEL_ARRIVAL)) {
                    JSONObject jsonObj = new JSONObject(retrievedData.get());
                    String status = jsonObj.getString("Status");
                    if (status.equals("1")) {
                        Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
                        loadFuelArrivals();
                    }
                } else if (type.equals(Constants.LOAD_FUEL_TYPES)) {
                    FuelType[] fuelTypes = new Gson().fromJson(retrievedData.get(), FuelType[].class);
                    for (FuelType fuelType : fuelTypes) {
                        fuelList.add(fuelType.getFuel());
                        fuelListKeys.add(String.valueOf(fuelType.getFid()));
                    }
                } else if (type.equals(Constants.UPDATE_FUEL_ARRIVAL_STATUS)) {
                    JSONObject jsonObj = new JSONObject(retrievedData.get());
                    String status = jsonObj.getString("Status");
                    if (status.equals("1")) {
                        Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                        loadFuelArrivals();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }

    private void updateSpinnerData() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, fuelList);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        fuelSpinner.setAdapter(dataAdapter);
    }
}