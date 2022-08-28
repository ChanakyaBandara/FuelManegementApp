package com.example.fuelmanegementapp.models;

public class Customer {
    //{"cid":"1","name":"test customer","nic":"98989833v","email":"testcus@gmail.com","phone":"77323232","address":"Kandy","lid":"1"}
    private int cid;
    private String name;
    private String nic;
    private String email;
    private String phone;
    private String address;
    private int lid;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }
}
