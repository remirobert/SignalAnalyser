package com.remirobert.remirobert.signalstrentgh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiaoShanhe on 2016/08/09.
 */
public class JRecord {
    private String mId;
    private JSignalRecord mSignalRecord;
    private JBattery mBattery;
    private JDevice mDevice;
    private List<JCellularTower> mCellularTowers;
    private double latitude;
    private double longitude;
    private long mDate;

    public JRecord(Record r) {
        mId = r.getId();
        mSignalRecord = new JSignalRecord(r.getSignalRecord());
        mBattery = new JBattery(r.getBattery());
        mDevice = new JDevice(r.getDevice());
        mCellularTowers = new ArrayList<>();
        for (CellularTower c : r.getCellularTowers()) {
            mCellularTowers.add(new JCellularTower(c));
        }
        latitude = r.getLatitude();
        longitude = r.getLongitude();
        mDate = r.getDate().getTime();
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

    public JBattery getmBattery() {
        return mBattery;
    }

    public void setmBattery(JBattery mBattery) {
        this.mBattery = mBattery;
    }

    public List<JCellularTower> getmCellularTowers() {
        return mCellularTowers;
    }

    public void setmCellularTowers(List<JCellularTower> mCellularTowers) {
        this.mCellularTowers = mCellularTowers;
    }

    public long getmDate() {
        return mDate;
    }

    public void setmDate(long mDate) {
        this.mDate = mDate;
    }

    public JDevice getmDevice() {
        return mDevice;
    }

    public void setmDevice(JDevice mDevice) {
        this.mDevice = mDevice;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public JSignalRecord getmSignalRecord() {
        return mSignalRecord;
    }

    public void setmSignalRecord(JSignalRecord mSignalRecord) {
        this.mSignalRecord = mSignalRecord;
    }

    private class JSignalRecord {
        int statutSignal;
        double noise;
        double evdoEci;
        int db;
        double level;
        boolean isGsm;

        public JSignalRecord(SignalRecord s) {
            statutSignal = s.getStatutSignal();
            noise = s.getNoise();
            evdoEci = s.getEvdoEci();
            db = s.getDb();
            level = s.getLevel();
            isGsm = s.isGsm();
        }

        public int getDb() {
            return db;
        }

        public void setDb(int db) {
            this.db = db;
        }

        public double getEvdoEci() {
            return evdoEci;
        }

        public void setEvdoEci(double evdoEci) {
            this.evdoEci = evdoEci;
        }

        public boolean isGsm() {
            return isGsm;
        }

        public void setGsm(boolean gsm) {
            isGsm = gsm;
        }

        public double getLevel() {
            return level;
        }

        public void setLevel(double level) {
            this.level = level;
        }

        public double getNoise() {
            return noise;
        }

        public void setNoise(double noise) {
            this.noise = noise;
        }

        public int getStatutSignal() {
            return statutSignal;
        }

        public void setStatutSignal(int statutSignal) {
            this.statutSignal = statutSignal;
        }
    }

    private class JBattery {
        int level;
        double capacity;

        public JBattery(Battery b) {
            level = b.getLevel();
            capacity = b.getCapacity();
        }

        public double getCapacity() {
            return capacity;
        }

        public void setCapacity(double capacity) {
            this.capacity = capacity;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }

    private class JDevice {
        String mOsVersion;
        String mApiLevel;
        String mModel;
        String mDevice;
        String mProduct;
        String mIMEI;
        String mIMSI;

        public JDevice(Device d) {
            mOsVersion = d.getOsVersion();
            mApiLevel = d.getApiLevel();
            mModel = d.getModel();
            mDevice = d.getDevice();
            mProduct = d.getProduct();
            mIMEI = d.getIMEI();
            mIMSI = d.getIMSI();
        }
    }

    private class JCellularTower {
        int mcc;
        int mnc;
        int lac;
        int cid;
        double lat;
        double lon;
        int asuLevel;
        int signalLevel;
        double signalDbm;

        public JCellularTower(CellularTower c) {
            mcc = c.getMcc();
            mnc = c.getMnc();
            lac = c.getLac();
            cid = c.getCid();
            lat = c.getLat();
            lon = c.getLon();
            asuLevel = c.getAsuLevel();
            signalLevel = c.getSignalLevel();
            signalDbm = c.getSignalDbm();
        }

        public int getAsuLevel() {
            return asuLevel;
        }

        public void setAsuLevel(int asuLevel) {
            this.asuLevel = asuLevel;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getLac() {
            return lac;
        }

        public void setLac(int lac) {
            this.lac = lac;
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

        public double getSignalDbm() {
            return signalDbm;
        }

        public void setSignalDbm(double signalDbm) {
            this.signalDbm = signalDbm;
        }

        public int getSignalLevel() {
            return signalLevel;
        }

        public void setSignalLevel(int signalLevel) {
            this.signalLevel = signalLevel;
        }
    }
}
