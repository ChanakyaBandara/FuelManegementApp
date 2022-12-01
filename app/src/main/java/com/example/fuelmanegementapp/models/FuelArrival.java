package com.example.fuelmanegementapp.models;

public class FuelArrival {
    //`fa_id`, `sid`, `ft_id`, `amount`, `timestamp`, `status`
    private int fa_id;
    private FuelStation fuelStation;
    private FuelType fuelType;
    private int amount;
    private String timestamp;
    private String status;

    public int getFa_id() {
        return fa_id;
    }

    public void setFa_id(int fa_id) {
        this.fa_id = fa_id;
    }

    public FuelStation getFuelStation() {
        return fuelStation;
    }

    public void setFuelStation(FuelStation fuelStation) {
        this.fuelStation = fuelStation;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
