package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
            Log.v(TAG, "Location changed...");
            Log.v(TAG, "Latitude :        " + location.getLatitude());
            Log.v(TAG, "Longitude :       " + location.getLongitude());
            //mLocationOnSubscribe.onNext(location);
            //mLocationOnSubscribe.onCompleted();
            //stopListening();
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
            //stopListening();
            mLocationOnSubscribe.onCompleted();
        }
    };

/*    private String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return mLocationManager.getBestProvider(criteria, true);
    }*/

    private void startListening() {
        Log.v(TAG, "start request listener");

        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLocationManager.requestSingleUpdate(android.location.LocationManager.GPS_PROVIDER, mLocationListener, null);

            String provider = android.location.LocationManager.GPS_PROVIDER;

            Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null) {
                Log.v(TAG, "lastKnownLocation not null");
                Log.v(TAG, lastKnownLocation.getLongitude() + ", " + lastKnownLocation.getLatitude());
                mLocationOnSubscribe.onNext(lastKnownLocation);
                mLocationOnSubscribe.onCompleted();
                return;
            } else {
                Log.e(TAG, "lastKnownLocation is null");

                mLocationOnSubscribe.onNext(null);
                mLocationOnSubscribe.onCompleted();
            }

            if (!mLocationManager.isProviderEnabled(provider)) {
                Log.v(TAG, "Provider not available = " + provider);
                mLocationOnSubscribe.onNext(null);
                mLocationOnSubscribe.onCompleted();
            }
        } else {
            Log.v(TAG, "Check permission location failed");
            mLocationOnSubscribe.onNext(null);
            mLocationOnSubscribe.onCompleted();
        }
    }

    private void stopListening() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    public Observable<Location> getLocation() {
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
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }
}
