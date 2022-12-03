package com.example.fuelmanegementapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class Login extends AppCompatActivity implements httpDataManager {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String nic = "nic";
    public static final String pass = "pass";
    public static final String sts = "sts";
    public static final String IP = "IP";
    private static int attempt = 0;
    EditText txtUsername, txtPassword;
    AlertDialog alertDialog;
    CheckBox logcheckBox;
    private Dialog myDialog;
    public static String IP_Address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDialog = new Dialog(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtpassword);
        logcheckBox = findViewById(R.id.logcheckBox);

        if (haveNetwork()) {
            load_user();
        } else {
            Toast.makeText(Login.this, "Unable to Connect to the Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void load_user() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        IP_Address = sharedPreferences.getString(IP, "");
        boolean sts_temp = sharedPreferences.getBoolean(sts, false);
        if (sts_temp && (attempt == 0)) {
            attempt++;
            String nic_temp = sharedPreferences.getString(nic, "");
            String pass_temp = sharedPreferences.getString(pass, "");
            login(nic_temp, pass_temp);
        }
    }

    private void login(String usernic, String userpass) {
        if (IP_Address != null && !IP_Address.isEmpty() && validate(IP_Address)) {
            if (!(TextUtils.isEmpty(usernic) && TextUtils.isEmpty(userpass))) {
                //Toast.makeText(Login.this, usernic+" "+userpass, Toast.LENGTH_SHORT).show();
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("type", Constants.LOGIN);
                param.put("Username", usernic);
                param.put("Password", userpass);
                BackgroundWorker backgroundworker = new BackgroundWorker(Login.this);
                backgroundworker.execute(param);
            } else {
                Toast.makeText(Login.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Login.this, "Please set valid IP address!", Toast.LENGTH_SHORT).show();
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
        finish();
    }

    public void goToStationRegister(View view) {
        Intent intent = new Intent(this, FuelStationRegister.class);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            String result = retrievedData.get();
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
                    if (Type.equals("2")) {
                        Intent intent = new Intent(this, CustomerDash.class);
                        String Extra_text1 = LID;
                        intent.putExtra("LID", Extra_text1);
                        this.startActivity(intent);
                        finish();
                    } else if (Type.equals("3")) {
                        Intent intent = new Intent(this, FuelStationDash.class);
                        String Extra_text1 = LID;
                        intent.putExtra("LID", Extra_text1);
                        this.startActivity(intent);
                        finish();
                    }
                } else {
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Login Status");
                    alertDialog.setMessage("Invalid Credentials");
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Login Status");
                alertDialog.setMessage("Error");
                alertDialog.show();
            }
        }

    }

    public static boolean validate(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }

    public void viewSettings(View view) {
        myDialog.setContentView(R.layout.custom_popup_pha);
        Button btnPopupBtn = myDialog.findViewById(R.id.btnPopupBtn);
        TextView btnPopupCurrentIP = myDialog.findViewById(R.id.btnPopupCurrentIP);
        TextView btnPopupNewIP = myDialog.findViewById(R.id.btnPopupNewIP);

        btnPopupCurrentIP.setText(IP_Address);

        btnPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String tempIP = btnPopupNewIP.getText().toString();
                if (tempIP != null && !tempIP.isEmpty() && validate(tempIP)) {
                    IP_Address = tempIP;
                    btnPopupCurrentIP.setText(IP_Address);
                    editor.putString(IP, tempIP);
                    editor.apply();
                    Toast.makeText(Login.this, "IP Address Updated !", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                } else {
                    Toast.makeText(Login.this, "Please set valid IP address!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}