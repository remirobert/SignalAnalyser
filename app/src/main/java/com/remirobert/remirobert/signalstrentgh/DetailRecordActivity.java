package com.remirobert.remirobert.signalstrentgh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import io.realm.Realm;
import io.realm.RealmQuery;

public class DetailRecordActivity extends AppCompatActivity {

    private MapView mMapView;
    private CellularTower mCellularTower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);

        MapboxAccountManager.start(this, "pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.setAccessToken("pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");
        mMapView.onCreate(savedInstanceState);

        String id = getIntent().getStringExtra("record");
        RealmQuery<CellularTower> query = Realm.getDefaultInstance().where(CellularTower.class);
        mCellularTower = query.contains("id", id).findFirst();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(mCellularTower.getLat(), mCellularTower.getLon()))
                                        .title("Tower : " + mCellularTower.getCid()));
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
