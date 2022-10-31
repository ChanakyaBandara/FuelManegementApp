package com.example.fuelmanegementapp.recycleviews.customer.vehicle;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.models.Vehicle;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleRecycleItem> {

    private List<Vehicle> mPer;
    private List<String> mKey;
    private AppCompatActivity mContext;

    public VehicleAdapter(List<Vehicle> mPer, List<String> mKey, AppCompatActivity mContext) {
        this.mPer = mPer;
        this.mKey = mKey;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VehicleRecycleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleRecycleItem(parent, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleRecycleItem holder, int position) {
        holder.bind(mPer.get(position), mKey.get(position));
    }

    @Override
    public int getItemCount() {
        return mPer.size();
    }
}
