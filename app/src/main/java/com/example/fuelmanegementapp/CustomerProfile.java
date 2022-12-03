package com.example.fuelmanegementapp;

import static com.example.fuelmanegementapp.Login.nic;
import static com.example.fuelmanegementapp.Login.pass;
import static com.example.fuelmanegementapp.Login.sts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Customer;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Optional;

public class CustomerProfile extends AppCompatActivity implements httpDataManager {

    private TextView txtName, txtPhone, txtNIC, txtEmail, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        txtName = findViewById(R.id.txtname);
        txtPhone = findViewById(R.id.txtPhone);
        txtNIC = findViewById(R.id.txtNIC);
        txtEmail = findViewById(R.id.txtemail);
        txtAddress = findViewById(R.id.txtAddress);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_CUSTOMER_DATA);
        param.put("LID", String.valueOf(CustomerDash.customer.getLid()));
        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerProfile.this);
        backgroundworker.execute(param);
    }

    public void goBack(View view) {
        Intent intent = new Intent(CustomerProfile.this, CustomerDash.class);
        startActivity(intent);
        finish();
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(Login.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(sts);
        editor.remove(nic);
        editor.remove(pass);
        editor.apply();
        Intent intent = new Intent(CustomerProfile.this, Login.class);
        CustomerDash.customer = null;
        CustomerDash.LID = null;
        startActivity(intent);
        finish();
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            Customer customer = new Gson().fromJson(retrievedData.get(), Customer.class);
            txtName.setText(customer.getName());
            txtEmail.setText(customer.getEmail());
            txtPhone.setText(customer.getPhone());
            txtNIC.setText(customer.getNic());
            txtAddress.setText(customer.getAddress());
        }
    }
}