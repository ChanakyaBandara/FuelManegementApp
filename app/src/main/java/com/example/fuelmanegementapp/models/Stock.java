package com.example.fuelmanegementapp.models;

import java.io.Serializable;

public class Stock implements Serializable {
    //`stid`, `sid`, `fid`, `available_amount`
    private int stid;
    private FuelStation station;
    private FuelType fuelType;
    private int available_amount;

    public Stock(int stid, FuelStation station, FuelType fuelType, int available_amount) {
        this.stid = stid;
        this.station = station;
        this.fuelType = fuelType;
        this.available_amount = available_amount;
    }

    public int getStid() {
        return stid;
    }

    public void setStid(int stid) {
        this.stid = stid;
    }

    public FuelStation getStation() {
        return station;
    }

    public void setStation(FuelStation station) {
        this.station = station;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public int getAvailable_amount() {
        return available_amount;
    }

    public void setAvailable_amount(int available_amount) {
        this.available_amount = available_amount;
    }
}
