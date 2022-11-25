package com.example.fuelmanegementapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelType;
import com.example.fuelmanegementapp.models.Stock;
import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

public class CustomerSpecialQr extends AppCompatActivity implements httpDataManager {

    private EditText txtSpecialPurpose, txtSpecialAmount, txtSpecialRef;
    private ImageView imageView;
    private String qr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_special_qr);

        txtSpecialPurpose = (EditText) findViewById(R.id.txtSpecialPurpose);
        txtSpecialAmount = (EditText) findViewById(R.id.txtSpecialAmount);
        txtSpecialRef = (EditText) findViewById(R.id.txtSpecialRef);
        imageView = (ImageView) findViewById(R.id.SPQrView);
        loadSPQR();
    }

    private void loadSPQR() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "get_special_qr");
        param.put("cid", String.valueOf(CustomerDash.customer.getCid()));
        Backgroundworker backgroundworker = new Backgroundworker(CustomerSpecialQr.this);
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
            Backgroundworker backgroundworker = new Backgroundworker(CustomerSpecialQr.this);
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
                    JSONArray jsonArray = new JSONArray(retrievedData.get());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        txtSpecialPurpose.setText(jsonObj.getString("purpose"));
                        txtSpecialAmount.setText(jsonObj.getString("amount"));
                        txtSpecialRef.setText(jsonObj.getString("ref"));
                        generateQRCode(jsonObj.getString("qr_code"));
                    }
                } else if (type.equals("add_special_qr")) {
                    Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
                    generateQRCode(qr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
}