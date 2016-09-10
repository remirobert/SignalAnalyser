package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by LiaoShanhe on 2016/09/07.
 */
public class ResourceProvider {
    private Context context;

    public ResourceProvider(Context context) {
        this.context = context;
    }

    public String[] getTravelingModes() {
        return context.getResources().getStringArray(R.array.travel_mode);
    }

    public boolean deleteOldDataOnExport() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("delete_old_on_export", false);
    }
}
