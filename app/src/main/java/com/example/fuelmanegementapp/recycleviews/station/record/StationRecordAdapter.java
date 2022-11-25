package com.example.fuelmanegementapp.recycleviews.station.record;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.models.Record;

import java.util.List;

public class StationRecordAdapter extends RecyclerView.Adapter<StationRecordRecycleItem> {

    private List<Record> mPer;
    private List<String> mKey;
    private AppCompatActivity mContext;

    public StationRecordAdapter(List<Record> mPer, List<String> mKey, AppCompatActivity mContext) {
        this.mPer = mPer;
        this.mKey = mKey;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StationRecordRecycleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StationRecordRecycleItem(parent, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull StationRecordRecycleItem holder, int position) {
        holder.bind(mPer.get(position), mKey.get(position));
    }

    @Override
    public int getItemCount() {
        return mPer.size();
    }
}
