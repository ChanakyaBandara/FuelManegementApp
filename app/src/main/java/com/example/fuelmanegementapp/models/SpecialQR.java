package com.example.fuelmanegementapp.models;

public class SpecialQR {
    //`sqr_id`, `cid`, `purpose`, `amount`, `approval`, `ref`, `qr_code`, `status`
    private int sqr_id;
    private Customer customer;
    private String purpose;
    private FuelType fuelType;
    private int amount;
    private int used;
    private int approval;
    private String ref;
    private String qr_code;
    private int status;

    public int getSqr_id() {
        return sqr_id;
    }

    public void setSqr_id(int sqr_id) {
        this.sqr_id = sqr_id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public int getUsed() {
        return used;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        this.approval = approval;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
