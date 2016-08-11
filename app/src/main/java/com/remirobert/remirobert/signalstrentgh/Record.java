package com.remirobert.remirobert.signalstrentgh;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by remirobert on 26/07/16.
 */
public class Record extends RealmObject {
    @PrimaryKey
    private String mId;
    private SignalRecord mSignalRecord;
    private Battery mBattery;
    private Device mDevice;
    private CellularTower mConnectedTower;
    private RealmList<CellularTower> mCellularTowers;
    private double latitude;
    private double longitude;
    private Date mDate;

    public Record() {
        mId = UUID.randomUUID().toString();
        mDate = new Date();
    }

    public Battery getBattery() {
        return mBattery;
    }

    public void setBattery(Battery battery) {
        mBattery = battery;
    }

    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice = device;
    }

    public Date getDate() {
        return mDate;
    }

    public SignalRecord getSignalRecord() {
        return mSignalRecord;
    }

    public void setSignalRecord(SignalRecord signalRecord) {
        mSignalRecord = signalRecord;
    }

    public RealmList<CellularTower> getCellularTowers() {
        return mCellularTowers;
    }

    public void setCellularTowers(RealmList<CellularTower> cellularTowers) {
        mCellularTowers = cellularTowers;
    }

    public CellularTower getConnectedTower() {
        return mConnectedTower;
    }

    public void setConnectedTower(CellularTower connectedTower) {
        mConnectedTower = connectedTower;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return mId;
    }

    @Override
    public String toString() {
        return "Record{" +
                "mId='" + mId + '\'' +
                ", mSignalRecord=" + mSignalRecord +
                ", mBattery=" + mBattery +
                ", mDevice=" + mDevice +
                ", mCellularTowers=" + mCellularTowers +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", mDate=" + mDate +
                '}';
    }
}
