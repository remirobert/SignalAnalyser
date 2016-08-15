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
    private String type;
    private int mcc;
    private int mnc;
    private int lac;
    private int cid;
    private int psc_or_pci;
    private double lat;
    private double lon;
    private int asuLevel;
    private int signalLevel;
    private double signalDbm;

    public CellularTower() {
        this.id = UUID.randomUUID().toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getPsc_or_pci() {
        return psc_or_pci;
    }

    public void setPsc_or_pci(int psc_or_pci) {
        this.psc_or_pci = psc_or_pci;
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

    @Override
    public String toString() {
        return "CellularTower{" +
                "asuLevel=" + asuLevel +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", mcc=" + mcc +
                ", mnc=" + mnc +
                ", lac=" + lac +
                ", cid=" + cid +
                ", psc_or_pci=" + psc_or_pci +
                ", lat=" + lat +
                ", lon=" + lon +
                ", signalLevel=" + signalLevel +
                ", signalDbm=" + signalDbm +
                '}';
    }
}
