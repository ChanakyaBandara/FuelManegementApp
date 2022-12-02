package com.example.fuelmanegementapp.util;

public class CommonUtils {
    public static String getStatus(String status) {
        switch (Integer.parseInt(status)) {
            case 0:
                return "Pending";
            case 1:
                return "On Route";
            case 2:
                return "Arrived";
            case 3:
                return "Will be delay";
            default:
                return "NA";
        }
    }
}
