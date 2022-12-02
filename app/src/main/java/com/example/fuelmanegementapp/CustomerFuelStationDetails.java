package com.example.fuelmanegementapp;

import static com.example.fuelmanegementapp.util.CommonUtils.getStatus;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelArrival;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Stock;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class CustomerFuelStationDetails extends AppCompatActivity implements httpDataManager {

    private FuelStation fuelStation;
    private TextView txtFSName, txtFSEmail, txtFSPhone, txtFSReg, txtFSCity, txtFSAddress, txtFSOpnCls, txtFSQueue;
    private TextView txtPetrolPercentage, txtDieselPercentage;
    private TextView txtSuperPetrolPercentage, txtSuperDieselPercentage;
    ProgressBar petrolProgressBar, dieselProgressBar;
    ProgressBar superPetrolProgressBar, superDieselProgressBar;
    private ArrayList<FuelArrival> fuelArrivalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_fuel_station_details);

        txtFSName = findViewById(R.id.txtFSName);
        txtFSEmail = findViewById(R.id.txtFSEmail);
        txtFSPhone = findViewById(R.id.txtFSPhone);
        txtFSReg = findViewById(R.id.txtFSReg);
//        txtFSCity = (TextView) findViewById(R.id.txtFSCity);
        txtFSAddress = findViewById(R.id.txtFSAddress);
        txtFSOpnCls = findViewById(R.id.txtFSOpnCls);
        txtFSQueue = findViewById(R.id.txtFSQueue);
        txtPetrolPercentage = findViewById(R.id.txtPetrolPercentage);
        txtSuperPetrolPercentage = findViewById(R.id.txtSuperPetrolPercentage);
        txtDieselPercentage = findViewById(R.id.txtDieselPercentage);
        txtSuperDieselPercentage = findViewById(R.id.txtSuperDieselPercentage);
        petrolProgressBar = findViewById(R.id.petrolProgressBar);
        superPetrolProgressBar = findViewById(R.id.superPetrolProgressBar);
        dieselProgressBar = findViewById(R.id.dieselProgressBar);
        superDieselProgressBar = findViewById(R.id.superDieselProgressBar);

        fuelArrivalList = new ArrayList<>();

        if (getIntent().getExtras() != null) {
            fuelStation = (FuelStation) getIntent().getSerializableExtra("FuelStationObj");
            populateStationData();
        }
    }

    private void loadFuelArrivals() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_FUEL_ARRIVALS);
        param.put("sid", String.valueOf(fuelStation.getSid()));

        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerFuelStationDetails.this);
        backgroundworker.execute(param);
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
        param.put("type", Constants.GET_STOCK_SID);
        param.put("SID", String.valueOf(fuelStation.getSid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerFuelStationDetails.this);
        backgroundworker.execute(param);
    }

    public void manageTable() {
        TableLayout tbl1 = findViewById(R.id.tblArrival);
        for (FuelArrival fuelArrival : fuelArrivalList) {
            TableRow tblrw = new TableRow(this);

            tblrw.setPadding(10, 10, 10, 10);

            TextView tv1 = new TextView(this);
            tv1.setText(fuelArrival.getTimestamp());
            tv1.setTextColor(Color.BLACK);
            tv1.setLayoutParams(new TableRow.LayoutParams(1));

            TextView tv2 = new TextView(this);
            tv2.setText(fuelArrival.getFuelType().getFuel());
            tv2.setTextColor(Color.BLACK);
            tv2.setLayoutParams(new TableRow.LayoutParams(2));

            TextView tv3 = new TextView(this);
            tv3.setText(String.valueOf(fuelArrival.getAmount()));
            tv3.setTextColor(Color.BLACK);
            tv3.setLayoutParams(new TableRow.LayoutParams(3));

            TextView tv4 = new TextView(this);
            tv4.setText(getStatus(fuelArrival.getStatus()));
            tv4.setTextColor(Color.BLACK);
            tv4.setLayoutParams(new TableRow.LayoutParams(4));

            tblrw.addView(tv1);
            tblrw.addView(tv2);
            tblrw.addView(tv3);
            tblrw.addView(tv4);
            tbl1.addView(tblrw);
        }
    }


    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            try {
                if (type.equals(Constants.GET_STOCK_SID)) {
                    JSONArray jsonArray = new JSONArray(retrievedData.get());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        FuelType fuelType = new FuelType(jsonObj.getInt("fid"), jsonObj.getString("fuel"));
                        Stock stock = new Stock(jsonObj.getInt("stid"), fuelStation, fuelType, jsonObj.getInt("available_amount"));

                        if (stock.getFuelType().getFid() == 1) {
                            petrolProgressBar.setProgress(stock.getAvailable_amount() * 100 / 10000);
                            txtPetrolPercentage.setText(stock.getAvailable_amount() * 100 / 10000 + " %");
                        } else if (stock.getFuelType().getFid() == 2) {
                            superPetrolProgressBar.setProgress(stock.getAvailable_amount() * 100 / 10000);
                            txtSuperPetrolPercentage.setText(stock.getAvailable_amount() * 100 / 10000 + " %");
                        } else if (stock.getFuelType().getFid() == 3) {
                            dieselProgressBar.setProgress(stock.getAvailable_amount() * 100 / 10000);
                            txtDieselPercentage.setText(stock.getAvailable_amount() * 100 / 10000 + " %");
                        } else if (stock.getFuelType().getFid() == 4) {
                            superDieselProgressBar.setProgress(stock.getAvailable_amount() * 100 / 10000);
                            txtSuperDieselPercentage.setText(stock.getAvailable_amount() * 100 / 10000 + " %");
                        }
                    }
                    loadFuelArrivals();
                } else if (type.equals(Constants.LOAD_FUEL_ARRIVALS)) {
                    fuelArrivalList.clear();
                    if (retrievedData.isPresent()) {
                        JSONArray jsonArray = new JSONArray(retrievedData.get());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FuelArrival fuelArrival = new Gson().fromJson(jsonArray.get(i).toString(), FuelArrival.class);
                            FuelType fuelType = new Gson().fromJson(jsonArray.get(i).toString(), FuelType.class);
                            fuelArrival.setFuelType(fuelType);
                            fuelArrivalList.add(fuelArrival);
                        }
                    }
                    if (fuelArrivalList.isEmpty()) {
                        Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
                    } else {
                        manageTable();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}