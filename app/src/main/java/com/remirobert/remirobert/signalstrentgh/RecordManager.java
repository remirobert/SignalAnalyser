package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import io.realm.Realm;
import rx.Observable;
import rx.functions.Func2;

/**
 * Created by remirobert on 26/07/16.
 */
public class RecordManager {

    private static final String TAG = "{RECORD manager}";

    private com.remirobert.remirobert.signalstrentgh.LocationManager mLocationManager;
    private SignalObserver mSignalObserver;
    private Context mContext;

    public Observable<Record> fetchRecord() {
        return Observable.combineLatest(mSignalObserver.observeOnce(), mLocationManager.getLocation(),
                new Func2<SignalRecord, Location, Record>() {
            @Override
            public Record call(SignalRecord signalRecord, Location location) {
                Log.v(TAG, "start creating record...");
                Record record = new Record();
                if (location != null) {
                    record.setLatitude(location.getLatitude());
                    record.setLongitude(location.getLongitude());
                }
                record.setSignalRecord(signalRecord);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(record);
                realm.commitTransaction();
                return record;
            }
        });
    }

    public RecordManager(Context context) {
        mContext = context;

        mLocationManager = new LocationManager(mContext);
        mSignalObserver = new SignalObserver(mContext);
    }
}
