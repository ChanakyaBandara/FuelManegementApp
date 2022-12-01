package com.example.fuelmanegementapp.recycleviews.station.fuelarrival;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.models.FuelArrival;

import java.util.List;

public class StationFuelArrivalAdapter extends RecyclerView.Adapter<StationFuelArrivalRecycleItem> {

    private List<FuelArrival> mPer;
    private List<String> mKey;
    private AppCompatActivity mContext;

    public StationFuelArrivalAdapter(List<FuelArrival> mPer, List<String> mKey, AppCompatActivity mContext) {
        this.mPer = mPer;
        this.mKey = mKey;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StationFuelArrivalRecycleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StationFuelArrivalRecycleItem(parent, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull StationFuelArrivalRecycleItem holder, int position) {
        holder.bind(mPer.get(position), mKey.get(position));
    }

    @Override
    public int getItemCount() {
        return mPer.size();
    }
}
