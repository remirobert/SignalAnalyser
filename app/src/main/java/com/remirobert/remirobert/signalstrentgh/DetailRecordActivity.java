package com.remirobert.remirobert.signalstrentgh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import io.realm.Realm;
import io.realm.RealmQuery;

public class DetailRecordActivity extends AppCompatActivity {

    private MapView mMapView;
    private Record mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);

        MapboxAccountManager.start(this, "pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.setAccessToken("pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");
        mMapView.onCreate(savedInstanceState);

        String id = getIntent().getStringExtra("record");
        RealmQuery<Record> query = Realm.getDefaultInstance().where(Record.class);
        mRecord = query.contains("mId", id).findFirst();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(mRecord.getLatitude(), mRecord.getLongitude())) // Sets the new camera position
                        .zoom(12)
                        .bearing(180)
                        .tilt(30)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mRecord.getLatitude(), mRecord.getLongitude()))
                        .title("Record position"));

                for (CellularTower tower : mRecord.getCellularTowers()) {
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(tower.getLat(), tower.getLon()))
                            .title("Tower : " + tower.getCid()));
                }
//                mapboxMap.addMarker(new MarkerOptions()
//                                        .position(new LatLng(mCellularTower.getLat(), mCellularTower.getLon()))
//                                        .title("Tower : " + mCellularTower.getCid()));
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
