package com.example.fuelmanegementapp.models;

public class Complaint {
    //`COID`, `timestamp`, `note`, `cid`
    private int COID;
    private String timestamp;
    private String note;

    public int getCOID() {
        return COID;
    }

    public void setCOID(int COID) {
        this.COID = COID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
