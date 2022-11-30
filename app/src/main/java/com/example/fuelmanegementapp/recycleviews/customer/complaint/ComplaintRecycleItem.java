package com.example.fuelmanegementapp.recycleviews.customer.complaint;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmanegementapp.R;
import com.example.fuelmanegementapp.models.Complaint;


public class ComplaintRecycleItem extends RecyclerView.ViewHolder {


    private final TextView txtComplaintNote;
    private final TextView txtComplaintDate;
    private String key;
    private Complaint complaint;

    public ComplaintRecycleItem(ViewGroup parent, AppCompatActivity mContext) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_complaint, parent, false));

        txtComplaintNote = itemView.findViewById(R.id.txtComplaintNote);
        txtComplaintDate = itemView.findViewById(R.id.txtComplaintDate);
    }

    public void bind(Complaint complaint, String key) {
        txtComplaintNote.setText(complaint.getNote());
        txtComplaintDate.setText(complaint.getTimestamp());
        this.complaint = complaint;
        this.key = key;
    }
}
