package com.example.fuelmanegementapp.services;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import com.example.fuelmanegementapp.interfaces.httpDataManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Optional;

public class Backgroundworker extends AsyncTask<HashMap<String, String>, Void, String> {

    private com.example.fuelmanegementapp.interfaces.httpDataManager httpDataManager;
    private Dialog myDialog;


    public Backgroundworker(httpDataManager parent) {
        httpDataManager = parent;
        if( parent instanceof AppCompatActivity){
            AppCompatActivity appCompatActivity = (AppCompatActivity) parent;
            myDialog = new Dialog(appCompatActivity);
        }
    }


    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        HashMap<String, String> param = params[0];
        String login_url = "http://192.168.1.4/Medco_Web/PHP/mobile.php";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = "";
            for (String name : param.keySet()) {
                String key = name;
                String value = param.get(name).toString();
                post_data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8") + "&";
            }
            if (post_data.length() > 0) {
                post_data = post_data.substring(0, post_data.length() - 1);
            }

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("Error_test1", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Error_test2", e.toString());
        } catch (Exception e) {
            Log.i("Error_test3", e.toString());
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
//        myDialog.setContentView(R.layout.activity_custome_pop_up);
//        ProgressBar progressBar = (ProgressBar) myDialog.findViewById(R.id.spin_kit);
//        Sprite wave = new Wave();
//        progressBar.setIndeterminateDrawable(wave);
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        myDialog.dismiss();
        if(result != null && result.length()>0){
            httpDataManager.retrieveData(Optional.of(result));
        }else {
            httpDataManager.retrieveData(Optional.empty());
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
