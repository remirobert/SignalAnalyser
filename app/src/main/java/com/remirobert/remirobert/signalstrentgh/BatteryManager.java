package com.remirobert.remirobert.signalstrentgh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import rx.Subscriber;

/**
 * Created by remirobert on 01/08/16.
 */
public class BatteryManager {

    private Context mContext;
    private BroadcastReceiver mBroadcastReceiver;

    public BatteryManager(Context context) {
        mContext = context;
    }

    private double getBatteryCapacity() {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
            return batteryCapacity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.MAX_VALUE;
    }

    public rx.Observable<Battery> getBatteryLevel() {
        return rx.Observable.create(new rx.Observable.OnSubscribe<Battery>() {
            @Override
            public void call(final Subscriber<? super Battery> subscriber) {
                mBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent intent) {
                        int rawlevel = intent.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1);
                        int scale = intent.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1);
                        int level = -1;
                        if (rawlevel >= 0 && scale > 0) {
                            level = (rawlevel * 100) / scale;
                        }
                        double batteryCapacity = getBatteryCapacity();
                        mContext.unregisterReceiver(mBroadcastReceiver);

                        Battery battery = new Battery();
                        battery.setCapacity(batteryCapacity);
                        battery.setLevel(level);
                        subscriber.onNext(battery);
                        subscriber.onCompleted();
                    }
                };
                mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            }
        });
    }
}
