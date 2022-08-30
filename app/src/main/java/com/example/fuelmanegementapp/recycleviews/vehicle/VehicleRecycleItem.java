package com.example.fuelmanegementapp.recycleviews.vehicle;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.CustomerViewVehicleDetails;
import com.example.fuelmanegementapp.R;
import com.example.fuelmanegementapp.models.Vehicle;

import java.io.Serializable;

public class VehicleRecycleItem extends RecyclerView.ViewHolder {

    private TextView txtRegNo, txtModal, txtBrand;
    private String key;
    private Vehicle vehicle;

    public VehicleRecycleItem(ViewGroup parent, AppCompatActivity mContext) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_vehicle, parent, false));
        txtRegNo = (TextView) itemView.findViewById(R.id.txtRegNo);
        txtModal = (TextView) itemView.findViewById(R.id.txtModal);
        txtBrand = (TextView) itemView.findViewById(R.id.txtBrand);

        itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CustomerViewVehicleDetails.class);
                    intent.putExtra("Extra_rec", (Serializable) vehicle);
                    mContext.startActivity(intent);
                }
            });
    }

    public void bind(Vehicle vehicle, String key) {
        txtRegNo.setText(vehicle.getReg_no());
        txtModal.setText(vehicle.getModel());
        txtBrand.setText(vehicle.getBrand());
        this.vehicle = vehicle;
        this.key = key;
    }
}
