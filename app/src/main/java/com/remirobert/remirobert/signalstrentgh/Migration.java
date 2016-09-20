package com.remirobert.remirobert.signalstrentgh;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by LiaoShanhe on 2016/09/13.
 */
public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        /*****************************************
         //  Version 0
         class Record
         @PrimaryKey private String mId;
         private SignalRecord mSignalRecord;
         private Battery mBattery;
         private Device mDevice;
         private CellularTower mConnectedTower;
         private RealmList<CellularTower> mCellularTowers;
         private double latitude;
         private double longitude;
         private Date mDate;
         private String mMode;
         *****************************************/
        RealmSchema schema = realm.getSchema();
    }
}
