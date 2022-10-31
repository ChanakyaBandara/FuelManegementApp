package com.example.fuelmanegementapp.recycleviews.customer.record;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.models.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordRecycleItem> {

    private List<Record> mPer;
    private List<String> mKey;
    private AppCompatActivity mContext;

    public RecordAdapter(List<Record> mPer, List<String> mKey, AppCompatActivity mContext) {
        this.mPer = mPer;
        this.mKey = mKey;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecordRecycleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordRecycleItem(parent, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordRecycleItem holder, int position) {
        holder.bind(mPer.get(position), mKey.get(position));
    }

    @Override
    public int getItemCount() {
        return mPer.size();
    }
}
