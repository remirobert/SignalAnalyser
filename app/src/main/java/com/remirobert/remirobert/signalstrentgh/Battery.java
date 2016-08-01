package com.remirobert.remirobert.signalstrentgh;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by remirobert on 01/08/16.
 */
public class Battery extends RealmObject {
    @PrimaryKey
    private String mId;
    private int level;
    private double capacity;

    public Battery() {
        mId = UUID.randomUUID().toString();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
}
