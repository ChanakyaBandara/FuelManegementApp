package com.example.fuelmanegementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fuelmanegementapp.R;
import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.services.Backgroundworker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class Login extends AppCompatActivity implements httpDataManager {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String nic = "nic";
    public static final String pass = "pass";
    public static final String sts = "sts";
    private static int attempt = 0;
    EditText txtUsername, txtPassword;
    AlertDialog alertDialog;
    CheckBox logcheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtpassword);
        logcheckBox = (CheckBox) findViewById(R.id.logcheckBox);

        if (haveNetwork()) {
            load_user();
        } else {
            Toast.makeText(Login.this, "Unable to Connect to the Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void load_user() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean sts_temp = sharedPreferences.getBoolean(sts, false);
        if (sts_temp && (attempt == 0)) {
            attempt++;
            String nic_temp = sharedPreferences.getString(nic, "");
            String pass_temp = sharedPreferences.getString(pass, "");
            login(nic_temp, pass_temp);
        }
    }

    private void login(String usernic, String userpass) {
        if (!(TextUtils.isEmpty(usernic) && TextUtils.isEmpty(userpass))) {
            //Toast.makeText(Login.this, usernic+" "+userpass, Toast.LENGTH_SHORT).show();
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "login");
            param.put("Username", usernic);
            param.put("Password", userpass);
            Backgroundworker backgroundworker = new Backgroundworker(Login.this);
            backgroundworker.execute(param);
        } else {
            Toast.makeText(Login.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void ForgetPass(View view) {
    }

    public void Login(View view) {
        if (haveNetwork()) {
            String Username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            login(Username, password);
        } else {
            Toast.makeText(Login.this, "Unable to Connect to the Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayName(String result) {

    }

    private boolean haveNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void goToCustomerRegister(View view) {
        Intent intent = new Intent(this, CustomerRegister.class);
        this.startActivity(intent);
    }

    public void goToStationRegister(View view) {
        Intent intent = new Intent(this, FuelStationRegister.class);
        this.startActivity(intent);
    }

    @Override
    public void retrieveData(Optional<String> retrievedData) {
        if(retrievedData.isPresent()){
            String result = retrievedData.get();
            Log.i("Error_Check",result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                String status = jsonObj.getString("Status");

                if (status.equals("1")) {
                    if (logcheckBox.isChecked()) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(nic, txtUsername.getText().toString());
                        editor.putString(pass, txtPassword.getText().toString());
                        editor.putBoolean(sts, true);

                        editor.apply();
                    }
                    String LID = jsonObj.getString("LID");
                    String Type = jsonObj.getString("Type");
                    if(Type.equals("2")){
                        Intent intent = new Intent(this, CustomerDash.class);
                        String Extra_text1 = LID;
                        intent.putExtra("Extra_text", Extra_text1);
                        this.startActivity(intent);
                    }else if(Type.equals("3")){
                        Intent intent = new Intent(this, FuelStationDash.class);
                        String Extra_text1 = LID;
                        intent.putExtra("Extra_text", Extra_text1);
                        this.startActivity(intent);
                    }
                } else {
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Login Status");
                    alertDialog.setMessage("Invalid Credintials");
                    alertDialog.show();
                }
            } catch (JSONException e) {
                Log.i("Error_Check",result);
                e.printStackTrace();
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Login Status");
                alertDialog.setMessage("Error");
                alertDialog.show();
            }
        }

    }
}