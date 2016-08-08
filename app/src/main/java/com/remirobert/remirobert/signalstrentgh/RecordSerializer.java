package com.remirobert.remirobert.signalstrentgh;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by LiaoShanhe on 2016/08/08.
 */
public class RecordSerializer implements JsonSerializer<Record> {
    @Override
    public JsonElement serialize(Record src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject record = new JsonObject();

        //mId
        record.addProperty("mId", src.getId());

        //SignalRecord
        JsonObject signalRecord = new JsonObject();
        signalRecord.addProperty("statutSignal", src.getSignalRecord().getStatutSignal());
        signalRecord.addProperty("noise", src.getSignalRecord().getNoise());
        signalRecord.addProperty("evdoEci", src.getSignalRecord().getEvdoEci());
        signalRecord.addProperty("db", src.getSignalRecord().getDb());
        signalRecord.addProperty("level", src.getSignalRecord().getLevel());
        signalRecord.addProperty("isGsm", src.getSignalRecord().isGsm());
        record.add("signalRecord", signalRecord);

        //Battery
        JsonObject battery = new JsonObject();
        battery.addProperty("level", src.getBattery().getLevel());
        battery.addProperty("capacity", src.getBattery().getCapacity());
        record.add("battery", battery);

        //Device
        JsonObject build = new JsonObject();
        build.addProperty("osVersion", src.getDevice().getOsVersion());
        build.addProperty("apiLevel", src.getDevice().getApiLevel());
        build.addProperty("model", src.getDevice().getModel());
        build.addProperty("device", src.getDevice().getDevice());
        build.addProperty("product", src.getDevice().getProduct());
        build.addProperty("IMEI", src.getDevice().getIMEI());
        build.addProperty("IMSI", src.getDevice().getIMSI());
        record.add("build", build);

        //List<CellularTower>
        JsonArray cellList = new JsonArray();
        for (CellularTower tower : src.getCellularTowers()) {
            JsonObject cellularTower = new JsonObject();
            cellularTower.addProperty("id", tower.getId());
            cellularTower.addProperty("mcc", tower.getMcc());
            cellularTower.addProperty("mnc", tower.getMnc());
            cellularTower.addProperty("lac", tower.getLac());
            cellularTower.addProperty("cid", tower.getCid());
            cellularTower.addProperty("lat", tower.getLat());
            cellularTower.addProperty("lon", tower.getLon());
            cellularTower.addProperty("asuLevel", tower.getAsuLevel());
            cellularTower.addProperty("signalLevel", tower.getSignalLevel());
            cellularTower.addProperty("signalDbm", tower.getSignalDbm());

            cellList.add(cellularTower);
        }
        record.add("cellularTowers", cellList);

        //latitude
        record.addProperty("latitude", src.getLatitude());

        //longitude
        record.addProperty("longitude", src.getLongitude());

        //Date
        record.addProperty("date", src.getDate().getTime());

        return record;
    }
}
