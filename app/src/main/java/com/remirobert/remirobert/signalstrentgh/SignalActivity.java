package com.remirobert.remirobert.signalstrentgh;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

public class SignalActivity extends AppCompatActivity {

    private static final String TAG = "SignalActivity";
    private static final int REQUEST_LOCATION = 1;

    private Handler mHandler;
    private boolean record = false;
    private RecordManager mRecordManager;
    private Runnable mDataCollection = new Runnable() {
        @Override
        public void run() {
            try {
                checkPermissionUser();
            } finally {
                SignalStrenght signalStrenght = (SignalStrenght) getApplication();
                mHandler.postDelayed(mDataCollection, signalStrenght.getmTimeInterval());
            }
        }
    };

    private void initListRecords() {
        Log.v(TAG, "init list records");
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Record> results = realm.where(Record.class).findAll();

        RecordAdapter recordAdapter = new RecordAdapter(results);
        rv.setAdapter(recordAdapter);
    }

    private void requestRecord() {
        mRecordManager.fetchRecord().subscribe(new Action1<Record>() {
            @Override
            public void call(Record record) {
                Log.v(TAG, "got record");
                initListRecords();
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
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (record) {
                        stopRecording();
                        record = false;
                        setTitle("SignalStrength" + "(stop)");
                    } else {
                        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        //long tInterval = Long.parseLong(sharedPref.getString(getString(R.string.pref_general_time_interval), "5"));
                        startRecording();
                        record = true;
                        setTitle("SignalStrength" + "(recording...)");
                    }
                }
            });
        }

        initListRecords();
        mRecordManager = new RecordManager(getApplicationContext());

        mHandler = new Handler();

    }

    private void startRecording() {
        mDataCollection.run();
    }

    private void stopRecording() {
        mHandler.removeCallbacks(mDataCollection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
