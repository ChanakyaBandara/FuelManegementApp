package com.example.fuelmanegementapp.recycleviews.customer.complaint;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.models.Complaint;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintRecycleItem> {

    private List<Complaint> mPer;
    private List<String> mKey;
    private AppCompatActivity mContext;

    public ComplaintAdapter(List<Complaint> mPer, List<String> mKey, AppCompatActivity mContext) {
        this.mPer = mPer;
        this.mKey = mKey;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ComplaintRecycleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComplaintRecycleItem(parent, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintRecycleItem holder, int position) {
        holder.bind(mPer.get(position), mKey.get(position));
    }

    @Override
    public int getItemCount() {
        return mPer.size();
    }
}
