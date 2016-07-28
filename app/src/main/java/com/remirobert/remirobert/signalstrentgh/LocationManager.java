package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by remirobert on 26/07/16.
 */
public class LocationManager {

    private static final String TAG = "[Location MANAGER]";
    private final static float LOCATION_REFRESH_DISTANCE = 0;
    private final static long LOCATION_REFRESH_TIME = 0;

    private Subscriber<? super Location> mLocationOnSubscribe;
    private android.location.LocationManager mLocationManager;
    private Context mContext;
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "get Location okay");
            mLocationOnSubscribe.onNext(location);
            mLocationOnSubscribe.onCompleted();
            stopListening();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.v(TAG, "changed status location : " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.v(TAG, "provider enabled : " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.v(TAG, "provider disabled : " + provider);
            stopListening();
            mLocationOnSubscribe.onCompleted();
        }
    };

    private void startListening() {
        mLocationManager =  (android.location.LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        Location location = mLocationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.v("Location manager", "get Location okay");
            mLocationOnSubscribe.onNext(location);
            mLocationOnSubscribe.onCompleted();
            return;
        }
        Log.v(TAG, "start request listener");
        mLocationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE,
                mLocationListener);
    }

    private void stopListening() {
        mLocationManager.removeUpdates(mLocationListener);
    }

    public Observable<Location>getLocation() {
        Log.v(TAG, "get location [permission okay]");
        return rx.Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(Subscriber<? super Location> subscriber) {
                Log.v(TAG, "start observer location");
                mLocationOnSubscribe = subscriber;
                startListening();
            }
        });
    }

    public LocationManager(Context context) {
        mContext = context;
    }
}
