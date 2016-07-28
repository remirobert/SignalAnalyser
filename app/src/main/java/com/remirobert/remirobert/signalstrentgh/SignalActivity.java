package com.remirobert.remirobert.signalstrentgh;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.functions.Action1;

public class SignalActivity extends AppCompatActivity {

    private static final String TAG = "SignalActivity";
    private static final int REQUEST_LOCATION = 1;

    private RecordManager mRecordManager;
    private CellTowerProximityInfo mCellTowerProximityInfo;
    private FloatingActionButton mFloatingActionButton;

    private void getTowerRecords() {
        Log.v(TAG, "Get tower cellular informations");
        mCellTowerProximityInfo.nerbyTower().subscribe(new Subscriber<List<CellularTower>>() {
            @Override
            public void onCompleted() {
                Log.v(TAG, "Completed");
                initListRecords();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Get error : " + e.getMessage());
            }

            @Override
            public void onNext(List<CellularTower> cellularTowers) {
                Log.v(TAG, "Get list info tower : " + cellularTowers.size());

            }
        });
    }

    private void initListRecords() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<CellularTower> results = realm.where(CellularTower.class).findAll();

        RecordAdapter recordAdapter = new RecordAdapter(results);
        rv.setAdapter(recordAdapter);
    }

    private void requestRecord() {
        mRecordManager.fetchRecord().subscribe(new Action1<Record>() {
            @Override
            public void call(Record record) {
                Log.v(TAG, "got record");
            }
        });
    }

    private void checkPermissionUser() {
        Log.v(TAG, "check location permission");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_LOCATION);
        } else {
            requestRecord();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestRecord();
            } else {
                Toast.makeText(this, "Error permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.button_new_record);

        initListRecords();
        mRecordManager = new RecordManager(getApplicationContext());

        mCellTowerProximityInfo = new CellTowerProximityInfo(getApplicationContext());

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionUser();
//                getTowerRecords();
            }
        });

        findViewById(R.id.loading_panel).setVisibility(View.GONE);
    }
}
