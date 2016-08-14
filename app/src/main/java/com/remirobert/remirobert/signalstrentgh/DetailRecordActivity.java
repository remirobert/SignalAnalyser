package com.remirobert.remirobert.signalstrentgh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

public class DetailRecordActivity extends AppCompatActivity {

    private final static String TAG = "DetailRecordActivity";

    private RecyclerView mListView;
    private MapView mMapView;
    private Record mRecord;

    private List<ListInfo> getDataAdapter() {
        List<ListInfo> listInfos = new ArrayList<>();

        ListInfo infoDate = new ListInfo();
        infoDate.setTitle("Record the :");
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        String date = format.format(java.sql.Date.parse(mRecord.getDate().toString()));
        infoDate.setContent(date);

        ListInfo infoAndroidVersion = new ListInfo();
        infoAndroidVersion.setTitle("Android version :");
        infoAndroidVersion.setContent(mRecord.getDevice().getOsVersion());

        ListInfo infoAndroidApi = new ListInfo();
        infoAndroidApi.setTitle("Android API level :");
        infoAndroidApi.setContent(mRecord.getDevice().getApiLevel());

        ListInfo infoDevice = new ListInfo();
        infoDevice.setTitle("Device model :");
        infoDevice.setContent(mRecord.getDevice().getDevice());

        ListInfo infoProduct = new ListInfo();
        infoProduct.setTitle("Product name :");
        infoProduct.setContent(mRecord.getDevice().getProduct());

        ListInfo infoCode1 = new ListInfo();
        infoCode1.setTitle("IMEI :");
        infoCode1.setContent(mRecord.getDevice().getIMEI());

        ListInfo infoCode2 = new ListInfo();
        infoCode2.setTitle("IMSI");
        infoCode2.setContent(mRecord.getDevice().getIMSI());

        ListInfo infoBatteryPercent = new ListInfo();
        infoBatteryPercent.setTitle("Battery percent :");
        infoBatteryPercent.setContent(mRecord.getBattery().getLevel() + "%");

        ListInfo infoBatteryCapacity = new ListInfo();
        infoBatteryCapacity.setTitle("Battery capacity :");
        infoBatteryCapacity.setContent(mRecord.getBattery().getCapacity() + " mha");

        ListInfo infoStatusSignal = new ListInfo();
        infoStatusSignal.setTitle("Signal status :");
        infoStatusSignal.setContent(mRecord.getSignalRecord().getStatutSignal() + "");

        ListInfo infoSignalNoise = new ListInfo();
        infoSignalNoise.setTitle("Signal noise :");
        infoSignalNoise.setContent(mRecord.getSignalRecord().getNoise() + " db");

        ListInfo infoSignalDb = new ListInfo();
        infoSignalDb.setTitle("Signal strength :");
        infoSignalDb.setContent(mRecord.getSignalRecord().getDb() + " db");

        ListInfo infoSignalEvdoEci = new ListInfo();
        infoSignalEvdoEci.setTitle("Signal :");
        infoSignalEvdoEci.setContent(mRecord.getSignalRecord().getEvdoEci() + "");

        ListInfo infoLocalisationSep = new ListInfo();
        infoLocalisationSep.setTitle("User localisation :");

        ListInfo infoLatitude = new ListInfo();
        infoLatitude.setTitle("Latitude :");
        infoLatitude.setContent(mRecord.getLatitude() + "");

        ListInfo infoLongitude = new ListInfo();
        infoLongitude.setTitle("Longitude :");
        infoLongitude.setContent(mRecord.getLongitude() + "");

        listInfos.add(infoDate);
        listInfos.add(infoAndroidVersion);
        listInfos.add(infoAndroidApi);
        listInfos.add(infoDevice);
        listInfos.add(infoProduct);
        listInfos.add(infoCode1);
        listInfos.add(infoCode2);
        listInfos.add(new ListInfo());
        listInfos.add(infoBatteryPercent);
        listInfos.add(infoBatteryCapacity);
        listInfos.add(new ListInfo());
        listInfos.add(infoStatusSignal);
        listInfos.add(infoSignalNoise);
        listInfos.add(infoSignalDb);
        listInfos.add(infoSignalEvdoEci);
        listInfos.add(new ListInfo());
        listInfos.add(infoLocalisationSep);
        listInfos.add(infoLatitude);
        listInfos.add(infoLongitude);
        listInfos.add(new ListInfo());

        ListInfo infoTitleTower = new ListInfo();
        infoTitleTower.setTitle("Cell tower list :");
        listInfos.add(infoTitleTower);

        for (CellularTower tower : mRecord.getCellularTowers()) {
            listInfos.add(new ListInfo());
            ListInfo sepTower = new ListInfo();
            sepTower.setTitle("----------------------");
            listInfos.add(sepTower);

            ListInfo lac = new ListInfo();
            lac.setTitle("LAC :");
            lac.setContent(tower.getLac() + "");
            listInfos.add(lac);

            ListInfo cid = new ListInfo();
            cid.setTitle("CID :");
            cid.setContent(tower.getCid() + "");
            listInfos.add(cid);

            ListInfo mcc = new ListInfo();
            mcc.setTitle("MCC :");
            mcc.setContent(tower.getMcc() + "");
            listInfos.add(mcc);

            ListInfo mnc = new ListInfo();
            mnc.setTitle("MNC :");
            mnc.setContent(tower.getMnc() + "");
            listInfos.add(mnc);

            ListInfo lat = new ListInfo();
            lat.setTitle("Latitude :");
            lat.setContent(tower.getLat() + "");
            listInfos.add(lat);

            ListInfo lon = new ListInfo();
            lon.setTitle("Longitude :");
            lon.setContent(tower.getLon() + "");
            listInfos.add(lon);

            ListInfo asuLevel = new ListInfo();
            asuLevel.setTitle("ASU level :");
            asuLevel.setContent(tower.getAsuLevel() + "");
            listInfos.add(asuLevel);

            ListInfo signalLevel = new ListInfo();
            signalLevel.setTitle("Signal level :");
            signalLevel.setContent(tower.getSignalLevel() + "");
            listInfos.add(signalLevel);

            ListInfo signalDbm = new ListInfo();
            signalDbm.setTitle("Signal DBM :");
            signalDbm.setContent(tower.getSignalDbm() + "");
            listInfos.add(signalDbm);
        }
        return listInfos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);

        String id = getIntent().getStringExtra("record");
        RealmQuery<Record> query = Realm.getDefaultInstance().where(Record.class);
        mRecord = query.contains("mId", id).findFirst();

        MapboxAccountManager.start(this, "pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");

        mMapView = (MapView) findViewById(R.id.mapView);
        mListView = (RecyclerView) findViewById(R.id.list);
        mListView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mListView.setLayoutManager(llm);

        DetailRecordAdapter adapter = new DetailRecordAdapter(getDataAdapter());
        mListView.setAdapter(adapter);

        mMapView.setAccessToken("pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");
        mMapView.onCreate(savedInstanceState);

        Log.v(TAG, "Record position : " + mRecord.getLatitude() + " : " + mRecord.getLongitude());

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(mRecord.getLatitude(), mRecord.getLongitude()))
                        .zoom(12)
                        .build();

                mapboxMap.setCameraPosition(position);

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mRecord.getLatitude(), mRecord.getLongitude()))
                        .title("Record position"));

                for (CellularTower tower : mRecord.getCellularTowers()) {
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(tower.getLat(), tower.getLon()))
                            .title("Tower : " + tower.getCid()));

                    List<LatLng> polygon = new ArrayList<>();
                    polygon.add(new LatLng(mRecord.getLatitude(), mRecord.getLongitude()));
                    polygon.add(new LatLng(mRecord.getLatitude() - 0.001, mRecord.getLongitude() - 0.001));
                    polygon.add(new LatLng(tower.getLat(), tower.getLon()));
                    polygon.add(new LatLng(tower.getLat() - 0.001, tower.getLon() - 0.001));

                    mapboxMap.addPolyline(new PolylineOptions().addAll(polygon).color(Color.parseColor("#3bb2d0")).width(2));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
