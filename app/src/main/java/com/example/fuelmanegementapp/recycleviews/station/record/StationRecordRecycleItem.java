package com.example.fuelmanegementapp.recycleviews.station.record;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.FuelStationRecords;
import com.example.fuelmanegementapp.R;
import com.example.fuelmanegementapp.models.Record;


public class StationRecordRecycleItem extends RecyclerView.ViewHolder {

    private final TextView txtVehicleReg;
    private final TextView txtRecDate;
    private final TextView txtRecFuel;
    private final TextView txtRecAmount;
    private final ImageButton imageButton;
    private String key;
    private Record record;

    public StationRecordRecycleItem(ViewGroup parent, AppCompatActivity mContext) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_station_record, parent, false));

        FuelStationRecords stationRecords = (FuelStationRecords) mContext;
        txtVehicleReg = itemView.findViewById(R.id.txtVehicleReg);
        txtRecDate = itemView.findViewById(R.id.txtRecDate);
        txtRecFuel = itemView.findViewById(R.id.txtRecFuel);
        txtRecAmount = itemView.findViewById(R.id.txtRecAmount);
        imageButton = itemView.findViewById(R.id.imgMarkCancel);

        imageButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               stationRecords.updateAsCancelled(record);
                                           }
                                       }
        );
    }

    public void bind(Record record, String key) {
        txtVehicleReg.setText(record.getVehicle().getReg_no());
        if (record.getStatus() == 1 ){
            txtVehicleReg.setText(record.getVehicle().getReg_no()+ "  Cancellation Requested");
            txtVehicleReg.setTextColor(Color.RED);
            imageButton.setClickable(false);
            imageButton.setVisibility(View.GONE);
        }else
        if (record.getStatus() == 2 ){
            txtVehicleReg.setText(record.getVehicle().getReg_no()+ "  Cancelled");
            txtVehicleReg.setTextColor(Color.RED);
            imageButton.setClickable(false);
            imageButton.setVisibility(View.GONE);
        }
        txtRecDate.setText(record.getTimestamp());
        txtRecAmount.setText(record.getAmount() + " L");
        txtRecFuel.setText(record.getVehicle().getFuelType().getFuel());
        this.record = record;
        this.key = key;
    }
}
