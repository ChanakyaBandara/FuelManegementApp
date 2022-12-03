package com.example.fuelmanegementapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.Complaint;
import com.example.fuelmanegementapp.recycleviews.RecycleViewConfig;
import com.example.fuelmanegementapp.recycleviews.customer.complaint.ComplaintAdapter;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomerComplaint extends AppCompatActivity implements httpDataManager {

    private List<Complaint> complaintList;
    private List<String> complaintIdList;
    private RecyclerView recyclerView;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint);

        complaintList = new ArrayList<Complaint>();
        complaintIdList = new ArrayList<String>();
        recyclerView = findViewById(R.id.recyclerView);
        myDialog = new Dialog(this);

        loadComplaints();
    }

    private void loadComplaints() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", Constants.LOAD_COMPLAINTS);
        param.put("cid", String.valueOf(CustomerDash.customer.getCid()));

        BackgroundWorker backgroundworker = new BackgroundWorker(CustomerComplaint.this);
        backgroundworker.execute(param);
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            if (type.equals(Constants.LOAD_COMPLAINTS)) {
                complaintList.clear();
                complaintIdList.clear();

                if (retrievedData.isPresent()) {
                    Complaint[] complaints = new Gson().fromJson(retrievedData.get(), Complaint[].class);
                    for (Complaint complaint : complaints) {
                        complaintList.add(complaint);
                        complaintIdList.add(String.valueOf(complaint.getCOID()));
                    }
                }
                if (complaintList.isEmpty()) {
                    Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
                } else {
                    ComplaintAdapter complaintAdapter = new ComplaintAdapter(complaintList, complaintIdList, this);
                    new RecycleViewConfig().setConfig(recyclerView, this, complaintAdapter);
                }
            }
            if (type.equals(Constants.ADD_COMPLAINT)) {
                loadComplaints();
            }
        }
    }

    public void goToAddComplaint(View view) {
        myDialog.setContentView(R.layout.custom_popup_complaint);
        Button btnPopupBtn = myDialog.findViewById(R.id.btnPopupBtn);
        TextView btnPopupComplaint = myDialog.findViewById(R.id.btnPopupComplaint);

        btnPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaintTxt = btnPopupComplaint.getText().toString();
                if (!TextUtils.isEmpty(complaintTxt)) {
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("type", Constants.ADD_COMPLAINT);
                    param.put("note", complaintTxt);
                    param.put("cid", String.valueOf(CustomerDash.customer.getCid()));

                    BackgroundWorker backgroundworker = new BackgroundWorker(CustomerComplaint.this);
                    backgroundworker.execute(param);
                    myDialog.dismiss();
                }
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}