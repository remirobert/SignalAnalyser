package com.remirobert.remirobert.signalstrentgh;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by remirobert on 01/08/16.
 */
public class Device extends RealmObject {
    @PrimaryKey
    private String mId;
    private String mOsVersion;
    private String mApiLevel;
    private String mModel;
    private String mDevice;
    private String mProduct;
    private String mIMEI;
    private String mIMSI;

    public Device() {
        mId = UUID.randomUUID().toString();
    }

    public String getOsVersion() {
        return mOsVersion;
    }

    public void setOsVersion(String osVersion) {
        mOsVersion = osVersion;
    }

    public String getApiLevel() {
        return mApiLevel;
    }

    public void setApiLevel(String apiLevel) {
        mApiLevel = apiLevel;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public String getDevice() {
        return mDevice;
    }

    public void setDevice(String device) {
        mDevice = device;
    }

    public String getProduct() {
        return mProduct;
    }

    public void setProduct(String product) {
        mProduct = product;
    }

    public String getIMEI() {
        return mIMEI;
    }

    public void setIMEI(String IMEI) {
        mIMEI = IMEI;
    }

    public String getIMSI() {
        return mIMSI;
    }

    public void setIMSI(String IMSI) {
        mIMSI = IMSI;
    }

    @Override
    public String toString() {
        return "Device{" +
                "mId='" + mId + '\'' +
                ", mOsVersion='" + mOsVersion + '\'' +
                ", mApiLevel='" + mApiLevel + '\'' +
                ", mModel='" + mModel + '\'' +
                ", mDevice='" + mDevice + '\'' +
                ", mProduct='" + mProduct + '\'' +
                ", mIMEI='" + mIMEI + '\'' +
                ", mIMSI='" + mIMSI + '\'' +
                '}';
    }
}
