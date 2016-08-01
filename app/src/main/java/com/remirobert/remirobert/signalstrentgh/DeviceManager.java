package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by remirobert on 01/08/16.
 */
public class DeviceManager {

    public Device information(Context context) {
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
        return device;
    }
}
