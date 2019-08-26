package com.albatross.deleveryapp;

import android.net.Uri;

import java.security.PrivateKey;

public class ModelClass {
    private String image;
    private String ordernumber;
    private String toAddress;
    private  String fromAddress;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;

    }


    private String status;
    public ModelClass(String ordernumber,String image, String toAddress, String fromAddress ,String status) {
        this.image = image;
        this.ordernumber = ordernumber;
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
