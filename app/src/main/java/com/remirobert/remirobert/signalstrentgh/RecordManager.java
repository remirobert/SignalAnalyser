package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Observable;
import rx.functions.Func4;

/**
 * Created by remirobert on 26/07/16.
 */
public class RecordManager {

    private static final String TAG = "{RECORD manager}";

    private com.remirobert.remirobert.signalstrentgh.LocationManager mLocationManager;
    private SignalObserver mSignalObserver;
    private CellTowerManager mCellTowerManager;
    private BatteryManager mBatteryManager;
    private Context mContext;

    public RecordManager(Context context) {
        mContext = context;

        mLocationManager = new LocationManager(mContext);
        mSignalObserver = new SignalObserver(mContext);
        mCellTowerManager = new CellTowerManager(mContext);
        mBatteryManager = new BatteryManager(mContext);
    }

    public Observable<Record> fetchRecord() {
        return Observable.combineLatest(mSignalObserver.observeOnce(),
                mLocationManager.getLocation(),
                mCellTowerManager.nerbyTower(),
                mBatteryManager.getBatteryLevel(),
                new Func4<SignalRecord, Location, List<CellularTower>, Battery, Record>() {
                    @Override
                    public Record call(SignalRecord signalRecord, Location location, List<CellularTower> cellularTowers, Battery battery) {
                        Log.v(TAG, "start creating record...");
                        Record record = new Record();
                        if (location != null) {
                            Log.v(TAG, "Location user : " + location.getLatitude() + " : " + location.getLongitude());
                            record.setLatitude(location.getLatitude());
                            record.setLongitude(location.getLongitude());
                        } else {
                            Log.v(TAG, "Location user failed");
                        }
                        if (cellularTowers != null) {
                            RealmList<CellularTower> cellularTowerRealmList = new RealmList<>();
                            for (CellularTower tower : cellularTowers) {
                                cellularTowerRealmList.add(tower);
                            }
                            record.setCellularTowers(cellularTowerRealmList);
                        }
                        record.setBattery(battery);
                        record.setDevice(DeviceManager.information(mContext));
                        record.setSignalRecord(signalRecord);
                        record.setConnectedTower(mCellTowerManager.getConnectedTower());

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(record);
                        realm.commitTransaction();
                        return record;
                    }
                });
    }
}
