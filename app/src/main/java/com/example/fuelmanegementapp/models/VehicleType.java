package com.example.fuelmanegementapp.models;

import java.io.Serializable;

public class VehicleType implements Serializable {
    //`vtid`, `type`, `description`, `allowed_quota`
    private int vtid;
    private String type;
    private String description;
    private int allowed_quota;

    public int getVtid() {
        return vtid;
    }

    public void setVtid(int vtid) {
        this.vtid = vtid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAllowed_quota() {
        return allowed_quota;
    }

    public void setAllowed_quota(int allowed_quota) {
        this.allowed_quota = allowed_quota;
    }
}
