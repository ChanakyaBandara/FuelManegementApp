package com.example.fuelmanegementapp.models;

import java.io.Serializable;

public class FuelType implements Serializable {
    //`fid`, `fuel`
    private int fid;
    private String fuel;

    public FuelType(String fuel) {
        this.fuel = fuel;
    }

    public FuelType(int fid, String fuel) {
        this.fid = fid;
        this.fuel = fuel;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
}
