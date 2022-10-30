package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Customer;

import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Optional;

public class CustomerProfile extends AppCompatActivity implements httpDataManager {

    private TextView txtName,txtPhone,txtNIC,txtEmail,txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        txtName = (TextView) findViewById(R.id.txtname);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtNIC = (TextView) findViewById(R.id.txtNIC);
        txtEmail = (TextView) findViewById(R.id.txtemail);
        txtAddress = (TextView) findViewById(R.id.txtAddress);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_customer_data");
        param.put("LID", String.valueOf(CustomerDash.customer.getLid()));
        Backgroundworker backgroundworker = new Backgroundworker(CustomerProfile.this, this.getApplicationContext());
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
        editor.clear();
        editor.apply();
        Intent intent = new Intent(CustomerProfile.this, Login.class);
        CustomerDash.customer = null;
        CustomerDash.LID = null;
        startActivity(intent);
        finish();
    }

    @Override
    public void retrieveData(String type,Optional<String> retrievedData) {
        if (retrievedData.isPresent()){
            Log.i("Error_Check",retrievedData.get());

            Customer customer = new Gson().fromJson(retrievedData.get(), Customer.class);

            txtName.setText(customer.getName());
            txtEmail.setText(customer.getEmail());
            txtPhone.setText(customer.getNic());
            txtNIC.setText(customer.getPhone());
            txtAddress.setText(customer.getAddress());
        }
    }
}