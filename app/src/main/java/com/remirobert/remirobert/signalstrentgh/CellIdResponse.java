package com.remirobert.remirobert.signalstrentgh;

import com.google.gson.annotations.SerializedName;

/**
 * Created by remirobert on 20/07/16.
 */
public class CellIdResponse {
    @SerializedName("lat")
    private double mLat;

    @SerializedName("lon")
    private double mLon;

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }
}
