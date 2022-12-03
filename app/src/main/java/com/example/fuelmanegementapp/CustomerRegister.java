package com.example.fuelmanegementapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;

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

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtNIC = findViewById(R.id.txtNIC);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtPassword = findViewById(R.id.txtPassword);
    }

    public void registration(View view) {
        String Name = txtName.getText().toString();
        String Email = txtEmail.getText().toString();
        String NIC = txtNIC.getText().toString();
        String Phone = txtPhone.getText().toString();
        String Address = txtAddress.getText().toString();
        String Password = txtPassword.getText().toString();

        if (!(TextUtils.isEmpty(Name) && TextUtils.isEmpty(NIC) && TextUtils.isEmpty(Address) && TextUtils.isEmpty(Email) && TextUtils.isEmpty(Phone) && TextUtils.isEmpty(Password) && TextUtils.isEmpty(NIC))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", Constants.ADD_CUSTOMER);
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
        if (retrievedData.isPresent()) {
            try {
                JSONObject jsonObj = new JSONObject(retrievedData.get());
                String status = jsonObj.getString("Status");
                if (status.equals("1")) {
                    String LID = jsonObj.getString("LID");
                    Intent intent = new Intent(this, CustomerDash.class);
                    intent.putExtra("LID", LID);
                    this.startActivity(intent);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Error_test1", e.toString());
            }
        }
    }
}