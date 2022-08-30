package com.example.fuelmanegementapp.models;

import java.io.Serializable;

public class Stock implements Serializable {
    //`stid`, `sid`, `fid`, `available_amount`
    private int stid;
    private FuelStation station;
    private FuelType fuelType;
    private int available_amount;
}
