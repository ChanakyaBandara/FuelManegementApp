package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Customer;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.SpecialQR;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationViewSPQrInfo extends AppCompatActivity implements httpDataManager {

    private TextView txtStaSQRCus, txtStaSQRAmount, txtStaSQRRef, txtStaSQRPurpose, txtStaSQRFuelType;
    private EditText txtStaSQRPumpedAmount;
    private SpecialQR specialQR;
    private int remainingQuota = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_view_spqr_info);
        String extra_qr = getIntent().getStringExtra("Extra_qr");

        txtStaSQRCus = (TextView) findViewById(R.id.txtStaSQRCus);
        txtStaSQRAmount = (TextView) findViewById(R.id.txtStaSQRAmount);
        txtStaSQRRef = (TextView) findViewById(R.id.txtStaSQRRef);
        txtStaSQRPurpose = (TextView) findViewById(R.id.txtStaSQRPurpose);
        txtStaSQRFuelType = (TextView) findViewById(R.id.txtStaSQRFuelType);
        txtStaSQRPumpedAmount = (EditText) findViewById(R.id.txtStaSQRPumpedAmount);

        loadSpecialQRByQR(extra_qr);
    }

    private void loadSpecialQRByQR(String extra_qr) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_specialQR_by_qr");
        param.put("qr", extra_qr);

        BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewSPQrInfo.this);
        backgroundworker.execute(param);
    }

    public void update(View view) {
        String pumpedAmount = txtStaSQRPumpedAmount.getText().toString();
        if (Integer.parseInt(pumpedAmount)!=0 && remainingQuota >= Integer.parseInt(pumpedAmount)) {
            if (!TextUtils.isEmpty(pumpedAmount)) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("type", "add_specialQR_record");
                param.put("sid", String.valueOf(FuelStationDash.fuelStation.getSid()));
                param.put("SPID", String.valueOf(specialQR.getSqr_id()));
                param.put("fid", String.valueOf(specialQR.getFuelType().getFid()));
                param.put("amount", pumpedAmount.trim());
                BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationViewSPQrInfo.this);
                backgroundworker.execute(param);

            } else {
                Toast.makeText(this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter allowed amount!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        Log.i("Error_Check", retrievedData.get().toString());
        if (retrievedData.isPresent()) {
            if (type.equals("load_specialQR_by_qr")) {
                specialQR = new Gson().fromJson(retrievedData.get(), SpecialQR.class);
                Customer customer = new Gson().fromJson(retrievedData.get(), Customer.class);
                FuelType fuelType = new Gson().fromJson(retrievedData.get(), FuelType.class);
                specialQR.setCustomer(customer);
                specialQR.setFuelType(fuelType);
                txtStaSQRCus.setText(specialQR.getCustomer().getName());
                remainingQuota = specialQR.getAmount() - specialQR.getUsed();
                txtStaSQRAmount.setText(String.valueOf(remainingQuota));
                txtStaSQRRef.setText(specialQR.getRef());
                txtStaSQRPurpose.setText(specialQR.getPurpose());
                txtStaSQRFuelType.setText(specialQR.getFuelType().getFuel());
            } else if (type.equals("add_specialQR_record")) {
                Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, FuelStationDash.class);
                this.startActivity(intent);
            }
        }
    }

}