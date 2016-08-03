package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by remirobert on 01/08/16.
 */
public class DeviceManager {

    private static final String TAG = "DeviceManager";

    public static Device information(Context context) {
        Device device = new Device();

        String serviceName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(serviceName);

        device.setIMEI(telephonyManager.getDeviceId());
        device.setIMSI(telephonyManager.getSubscriberId());

        device.setOsVersion(System.getProperty("os.version"));
        device.setApiLevel(android.os.Build.VERSION.RELEASE);
        device.setModel(android.os.Build.MODEL);
        device.setProduct(android.os.Build.PRODUCT);
        device.setDevice(android.os.Build.DEVICE);

        Log.v(TAG, device.getApiLevel());
        Log.v(TAG, device.getDevice());
        Log.v(TAG, device.getIMEI());
        Log.v(TAG, device.getIMSI());
        Log.v(TAG, device.getModel());
        Log.v(TAG, device.getOsVersion());
        Log.v(TAG, device.getProduct());

        return device;
    }
}
