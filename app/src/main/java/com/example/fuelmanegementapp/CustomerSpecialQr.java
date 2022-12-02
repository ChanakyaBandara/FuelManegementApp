package com.example.fuelmanegementapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.SpecialQR;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

public class CustomerSpecialQr extends AppCompatActivity implements httpDataManager {

    private EditText txtSpecialPurpose, txtSpecialAmount, txtSpecialRef, txtSpecialRemAmount;
    private View layoutSpecialRemAmount;
    private Button btnSPQRSubmit, btnSPQRDelete;
    private SpecialQR specialQR;
    private ImageView imageView;
    private String qr = "";
    private AppCompatSpinner fuelSpinner;
    private ArrayList<String> fuelList;
    private ArrayList<String> fuelListKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_special_qr);

        txtSpecialPurpose = (EditText) findViewById(R.id.txtSpecialPurpose);
        txtSpecialAmount = (EditText) findViewById(R.id.txtSpecialAmount);
        txtSpecialRef = (EditText) findViewById(R.id.txtSpecialRef);
        txtSpecialRemAmount = (EditText) findViewById(R.id.txtSpecialRemAmount);
        layoutSpecialRemAmount = findViewById(R.id.layoutSpecialRemAmount);
        btnSPQRSubmit = findViewById(R.id.btnSPQRSubmit);
        btnSPQRDelete = findViewById(R.id.btnSPQRDelete);
        imageView = (ImageView) findViewById(R.id.SPQrView);

        fuelSpinner = (AppCompatSpinner) findViewById(R.id.fuelDrop);
        fuelSpinner.setPrompt("Choose Fuel Type");

        fuelList = new ArrayList<String>();
        fuelListKeys = new ArrayList<String>();

        loadFuelSpinnerData();
    }

    private void loadFuelSpinnerData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_fuel_types");
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerSpecialQr.this);
        backgroundworker.execute(param);
    }

    private void loadSPQR() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "get_special_qr");
        param.put("cid", String.valueOf(CustomerDash.customer.getCid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerSpecialQr.this);
        backgroundworker.execute(param);
    }

    public void requestQR(View view) {
        String purpose = txtSpecialPurpose.getText().toString();
        String amount = txtSpecialAmount.getText().toString();
        String ref = txtSpecialRef.getText().toString();
        qr = "SPQR#" + Calendar.getInstance().getTimeInMillis();
        if (!(TextUtils.isEmpty(purpose) && TextUtils.isEmpty(amount) && TextUtils.isEmpty(ref))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "add_special_qr");
            param.put("purpose", purpose);
            param.put("amount", amount);
            param.put("ref", ref);
            param.put("qr_code", qr);
            param.put("cid", String.valueOf(CustomerDash.customer.getCid()));
            BackgroundWorker backgroundworker = new BackgroundWorker(CustomerSpecialQr.this);
            backgroundworker.execute(param);
        } else {
            Toast.makeText(CustomerSpecialQr.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            try {
                if (type.equals("get_special_qr")) {
                    if(!retrievedData.get().isEmpty()){
                        btnSPQRSubmit.setVisibility(View.GONE);
                        btnSPQRDelete.setVisibility(View.VISIBLE);
                        specialQR = new Gson().fromJson(retrievedData.get(), SpecialQR.class);
                        FuelType fuelType = new Gson().fromJson(retrievedData.get(), FuelType.class);
                        specialQR.setFuelType(fuelType);

                        txtSpecialPurpose.setText(specialQR.getPurpose());
                        txtSpecialPurpose.setEnabled(false);
                        txtSpecialAmount.setText(String.valueOf(specialQR.getAmount()));
                        txtSpecialAmount.setEnabled(false);
                        txtSpecialRef.setText(specialQR.getRef());
                        txtSpecialRef.setEnabled(false);
                        int remainingAmount = specialQR.getAmount() - specialQR.getUsed();

                        if(specialQR.getApproval()==1){
                            generateQRCode(specialQR.getQr_code());
                            txtSpecialRemAmount.setText(String.valueOf(remainingAmount));
                        }else {
                            txtSpecialRemAmount.setText("Approval Pending!");
                            txtSpecialRemAmount.setTextColor(Color.GREEN);
                        }

                        layoutSpecialRemAmount.setVisibility(View.VISIBLE);
                        txtSpecialRemAmount.setEnabled(false);

                        fuelList.clear();
                        fuelListKeys.clear();
                        fuelList.add(fuelType.getFuel());
                        fuelListKeys.add(String.valueOf(fuelType.getFid()));
                        updateFuelSpinner();
                    }else {
                        btnSPQRSubmit.setVisibility(View.VISIBLE);
                        btnSPQRDelete.setVisibility(View.GONE);
                    }
                } else if (type.equals("add_special_qr")) {
                    Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
                    generateQRCode(qr);
                } else if (type.equals("remove_special_qr")) {
                    Toast.makeText(this, "Successfully removed!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, FuelStationDash.class);
                    this.startActivity(intent);
                } else if (type.equals("load_fuel_types")) {
                    FuelType[] fuelTypes = new Gson().fromJson(retrievedData.get(), FuelType[].class);

                    for (FuelType fuelType : fuelTypes) {
                        fuelList.add(fuelType.getFuel());
                        fuelListKeys.add(String.valueOf(fuelType.getFid()));
                    }

                    updateFuelSpinner();
                    loadSPQR();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateFuelSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, fuelList);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        fuelSpinner.setAdapter(dataAdapter);
    }


    private void generateQRCode(String data) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeQR(View view) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "remove_special_qr");
        param.put("SPID", String.valueOf(specialQR.getSqr_id()));
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerSpecialQr.this);
        backgroundworker.execute(param);
    }
}