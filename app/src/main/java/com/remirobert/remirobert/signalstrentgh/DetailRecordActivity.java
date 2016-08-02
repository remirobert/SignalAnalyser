package com.remirobert.remirobert.signalstrentgh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

public class DetailRecordActivity extends AppCompatActivity {

    private final static String TAG = "DetailRecordActivity";

    private ListView mListView;
    private MapView mMapView;
    private Record mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);

        MapboxAccountManager.start(this, "pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");

        mMapView = (MapView) findViewById(R.id.mapView);
        mListView = (ListView) findViewById(R.id.list);

        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, android.R.id.text1, values);


        // Assign adapter to ListView
        mListView.setAdapter(adapter);

        mMapView.setAccessToken("pk.eyJ1IjoicmVtaXJvYmVydDMzNTMwIiwiYSI6ImNpcjF4eDc1dTAwOGpodW5uYmp1b2VqZ2sifQ.uqX-g4VqZxPTsOK0qx77KQ");
        mMapView.onCreate(savedInstanceState);

        String id = getIntent().getStringExtra("record");
        RealmQuery<Record> query = Realm.getDefaultInstance().where(Record.class);
        mRecord = query.contains("mId", id).findFirst();

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
