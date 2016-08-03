package com.remirobert.remirobert.signalstrentgh;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by remirobert on 19/07/16.
 */
public class CellularTower extends RealmObject implements Serializable {
    @PrimaryKey
    private String id;
    private int mcc;
    private int mnc;
    private int lac;
    private int cid;
    private double lat;
    private double lon;
    private int asuLevel;
    private int signalLevel;
    private double signalDbm;

    public CellularTower() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getAsuLevel() {
        return asuLevel;
    }

    public void setAsuLevel(int asuLevel) {
        this.asuLevel = asuLevel;
    }

    public int getSignalLevel() {
        return signalLevel;
    }

    public void setSignalLevel(int signalLevel) {
        this.signalLevel = signalLevel;
    }

    public double getSignalDbm() {
        return signalDbm;
    }

    public void setSignalDbm(double signalDbm) {
        this.signalDbm = signalDbm;
    }
}
