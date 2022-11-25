package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.services.BackgroundWorker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class CustomerRegister extends AppCompatActivity implements httpDataManager {

    private EditText txtName, txtNIC, txtEmail, txtPhone, txtPassword, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtNIC = (EditText) findViewById(R.id.txtNIC);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    public void registration(View view) {
        String Name = txtName.getText().toString();
        String Email = txtEmail.getText().toString();
        String NIC = txtNIC.getText().toString();
        String Phone = txtPhone.getText().toString();
        String Address = txtAddress.getText().toString();
        String Password = txtPassword.getText().toString();

        if (!(TextUtils.isEmpty(Name) && TextUtils.isEmpty(NIC) && TextUtils.isEmpty(Address) && TextUtils.isEmpty(Email) && TextUtils.isEmpty(Phone) && TextUtils.isEmpty(Password)  && TextUtils.isEmpty(NIC))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "addCustomer");
            param.put("name", Name);
            param.put("email", Email);
            param.put("nic", NIC.toLowerCase().trim());
            param.put("address", Address);
            param.put("phone", Phone.trim());
            param.put("Password", Password);
            BackgroundWorker backgroundworker = new BackgroundWorker(CustomerRegister.this);
            backgroundworker.execute(param);

        } else {
            Toast.makeText(this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()){
            try {
                JSONObject jsonObj = new JSONObject(retrievedData.get());
                String status = jsonObj.getString("Status");
                if (status.equals("1")) {
                    String LID = jsonObj.getString("LID");
                    Intent intent = new Intent(this, CustomerDash.class);
                    intent.putExtra("LID", LID);
                    this.startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("testingerror", e.toString());
            }
        }
    }
}