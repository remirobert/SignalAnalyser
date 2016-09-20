package com.remirobert.remirobert.signalstrentgh;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by remirobert on 23/07/16.
 */
public class SignalStrenght extends Application {

    private long mTimeInterval; // in milliseconds
    public SharedPreferences.OnSharedPreferenceChangeListener
            onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.pref_general_time_interval))) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                long tInterval = Long.parseLong(sharedPref.getString(
                        getString(R.string.pref_general_time_interval), getString(R.string.pref_default_time_interval_seconds)));
                if (tInterval <= 0)
                    return;
                setmTimeInterval(tInterval * 1000);
            }
        }
    };

    public long getmTimeInterval() {
        return mTimeInterval;
    }

    public void setmTimeInterval(long mTimeInterval) {
        this.mTimeInterval = mTimeInterval;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .schemaVersion(0)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long tInterval = Long.parseLong(sharedPref.getString(
                getString(R.string.pref_general_time_interval), getString(R.string.pref_default_time_interval_seconds)));
        mTimeInterval = tInterval <= 0 ? 30000 : tInterval * 1000;
    }
}
