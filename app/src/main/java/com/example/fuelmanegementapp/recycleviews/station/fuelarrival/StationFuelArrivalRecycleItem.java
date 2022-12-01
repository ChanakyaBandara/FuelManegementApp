package com.example.fuelmanegementapp.recycleviews.station.fuelarrival;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.FuelStationArrival;
import com.example.fuelmanegementapp.R;
import com.example.fuelmanegementapp.models.FuelArrival;


public class StationFuelArrivalRecycleItem extends RecyclerView.ViewHolder {

    private FuelStationArrival fuelStationArrival;
    private final TextView txtFAAmount, txtFAFuel, txtFADate, txtFAStatus;
    private final ImageButton imageButton;
    private String key;
    private FuelArrival fuelArrival;

    public StationFuelArrivalRecycleItem(ViewGroup parent, AppCompatActivity mContext) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_station_fuel_arrival, parent, false));

        fuelStationArrival = (FuelStationArrival)mContext;
        txtFAAmount = itemView.findViewById(R.id.txtFAAmount);
        txtFAFuel = itemView.findViewById(R.id.txtFAFuel);
        txtFADate = itemView.findViewById(R.id.txtFADate);
        txtFAStatus = itemView.findViewById(R.id.txtFAStatus);
        imageButton = itemView.findViewById(R.id.imgMarkArrived);

        imageButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               fuelStationArrival.updateAsArrived(fuelArrival);
                                           }
                                       }
        );
    }

    public void bind(FuelArrival fuelArrival, String key) {
        txtFAAmount.setText(String.valueOf(fuelArrival.getAmount()));
        txtFAFuel.setText(fuelArrival.getFuelType().getFuel());
        txtFADate.setText(fuelArrival.getTimestamp());
        txtFAStatus.setText(getStatus(fuelArrival.getStatus()));
        if (fuelArrival.getStatus().equals("2")){
            imageButton.setClickable(false);
            imageButton.setVisibility(View.GONE);
        }
        this.fuelArrival = fuelArrival;
        this.key = key;
    }

    private String getStatus(String status) {
        switch (Integer.parseInt(status)) {
            case 0:
                return "Pending";
            case 1:
                return "On Route";
            case 2:
                return "Arrived";
            case 3:
                return "Will be delay";
            default:
                return "NA";
        }
    }
}
