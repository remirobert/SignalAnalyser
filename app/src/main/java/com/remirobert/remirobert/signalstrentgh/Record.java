package com.remirobert.remirobert.signalstrentgh;

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
    private RealmList<CellularTower> mCellularTowers;
    private double latitude;
    private double longitude;

    public Record() {
        mId = UUID.randomUUID().toString();
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
}
