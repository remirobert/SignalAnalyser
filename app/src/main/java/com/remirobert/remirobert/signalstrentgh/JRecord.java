package com.remirobert.remirobert.signalstrentgh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiaoShanhe on 2016/08/09.
 */
public class JRecord {
    private String id;
    private JSignalRecord signalRecord;
    private JBattery battery;
    private JDevice device;
    private JCellularTower connectedTower;
    private List<JCellularTower> cellularTowers;
    private double latitude;
    private double longitude;
    private long date;

    public JRecord(Record r) {
        id  = r.getId();
        signalRecord = new JSignalRecord(r.getSignalRecord());
        battery = new JBattery(r.getBattery());
        device = new JDevice(r.getDevice());
        cellularTowers = new ArrayList<>();
        for (CellularTower c : r.getCellularTowers()) {
            cellularTowers.add(new JCellularTower(c));
        }
        connectedTower = new JCellularTower(r.getConnectedTower());
        latitude = r.getLatitude();
        longitude = r.getLongitude();
        date = r.getDate().getTime();
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

    public JSignalRecord getSignalRecord() {
        return signalRecord;
    }

    public void setSignalRecord(JSignalRecord signalRecord) {
        this.signalRecord = signalRecord;
    }

    public JBattery getBattery() {
        return battery;
    }

    public void setBattery(JBattery battery) {
        this.battery = battery;
    }

    public JDevice getDevice() {
        return device;
    }

    public void setDevice(JDevice device) {
        this.device = device;
    }

    public List<JCellularTower> getCellularTowers() {
        return cellularTowers;
    }

    public void setCellularTowers(List<JCellularTower> cellularTowers) {
        this.cellularTowers = cellularTowers;
    }

    public JCellularTower getConnectedTower() {
        return connectedTower;
    }

    public void setConnectedTower(JCellularTower connectedTower) {
        this.connectedTower = connectedTower;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        String osVersion;
        String apiLevel;
        String model;
        String device;
        String product;
        String IMEI;
        String IMSI;

        public JDevice(Device d) {
            osVersion = d.getOsVersion();
            apiLevel = d.getApiLevel();
            model = d.getModel();
            device = d.getDevice();
            product = d.getProduct();
            IMEI = d.getIMEI();
            IMSI = d.getIMSI();
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
