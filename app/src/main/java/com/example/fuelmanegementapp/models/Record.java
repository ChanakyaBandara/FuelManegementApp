package com.example.fuelmanegementapp.models;

import java.io.Serializable;

public class Record implements Serializable {
    //`rid`, `timestamp`, `vid`, `sid`, `amount`
    private int rid;
    private String timestamp;
    private Vehicle vehicle;
    private FuelStation fuelStation;
    private int amount;

    public Record(int rid, String timestamp, Vehicle vehicle, FuelStation fuelStation, int amount) {
        this.rid = rid;
        this.timestamp = timestamp;
        this.vehicle = vehicle;
        this.fuelStation = fuelStation;
        this.amount = amount;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public FuelStation getFuelStation() {
        return fuelStation;
    }

    public void setFuelStation(FuelStation fuelStation) {
        this.fuelStation = fuelStation;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
