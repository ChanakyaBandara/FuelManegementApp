package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.services.Backgroundworker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

public class CustomerAddVehicle extends AppCompatActivity implements httpDataManager {

    private EditText txtInCusVehReg, txtInCusVehBrand, txtInCusVehModal, txtInCusVehEngine, txtInCusVehChassis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_vehicle);

        txtInCusVehReg = (EditText) findViewById(R.id.txtInCusVehReg);
        txtInCusVehBrand = (EditText) findViewById(R.id.txtInCusVehBrand);
        txtInCusVehModal = (EditText) findViewById(R.id.txtInCusVehModal);
        txtInCusVehEngine = (EditText) findViewById(R.id.txtInCusVehEngine);
        txtInCusVehChassis = (EditText) findViewById(R.id.txtInCusVehChassis);
    }

    public void registerVehicle(View view) {
        String regNo = txtInCusVehReg.getText().toString();
        String brand = txtInCusVehBrand.getText().toString();
        String modal = txtInCusVehModal.getText().toString();
        String engine = txtInCusVehEngine.getText().toString();
        String chassis = txtInCusVehChassis.getText().toString();
        if (!(TextUtils.isEmpty(regNo) && TextUtils.isEmpty(brand) && TextUtils.isEmpty(modal) && TextUtils.isEmpty(engine) && TextUtils.isEmpty(chassis))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "addVehicle");
            param.put("regNo", regNo);
            param.put("brand", brand);
            param.put("modal", modal);
            param.put("engine", engine);
            param.put("chassis", chassis);
            param.put("qr", regNo+Calendar.getInstance().getTimeInMillis());
            param.put("vtid", "1");
            param.put("cid", String.valueOf(CustomerDash.customer.getCid()));
            param.put("fid", "1");
            Backgroundworker backgroundworker = new Backgroundworker(CustomerAddVehicle.this, this.getApplicationContext());
            backgroundworker.execute(param);
        } else {
            Toast.makeText(CustomerAddVehicle.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()){
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CustomerViewVehicles.class);
            this.startActivity(intent);
        }

    }
}