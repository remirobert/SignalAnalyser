package com.remirobert.remirobert.signalstrentgh;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by remirobert on 15/07/16.
 */
public class SignalRecord extends RealmObject {

    @Ignore
    public static final int SIGNAL_STATUT_OK = 0;
    @Ignore
    public static final int SIGNAL_STATUT_NONE = 1;

    private int statutSignal;
    private double noise;
    private double evdoEci;
    private int db;
    private double level;
    private boolean isGsm;

    public SignalRecord() {
    }

    public int getStatutSignal() {
        return statutSignal;
    }

    public void setStatutSignal(int statutSignal) {
        this.statutSignal = statutSignal;
    }

    public boolean isGsm() {
        return isGsm;
    }

    public void setGsm(boolean gsm) {
        isGsm = gsm;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public double getEvdoEci() {
        return evdoEci;
    }

    public void setEvdoEci(double evdoEci) {
        this.evdoEci = evdoEci;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }
}
