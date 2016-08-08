package com.remirobert.remirobert.signalstrentgh;

import android.Manifest;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

public class SignalActivity extends AppCompatActivity {

    private static final String TAG = "SignalActivity";
    private static final int REQUEST_LOCATION = 1;

    private long mInterval = 5000;
    private Handler mHandler;
    private boolean record = false;

    private RecordManager mRecordManager;
    //private FloatingActionButton mFloatingActionButton;

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

    //==================
    public void exportRealmData(final RealmResults<Record> results) {
        // Realm realm = Realm.getDefaultInstance();
        //RealmResults<Record> results = realm.where(Record.class).findAll();
        Gson gson = new Gson();
        //for (Record r : results) {
        Record r = results.get(0);
        String json = gson.toJson(r);
        Log.e("大大的", json);
        //}
//        String json = gson.toJson(results);
//        Log.e("大大的", json);
    }
    //==================

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
                        stopRepeatingTask();
                        record = false;
                    } else {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        long tInterval = Long.parseLong(sharedPref.getString("time_interval", "5"));
                        Log.e("聊啥呢", tInterval + ">>>>>>>>>>>>>>>>>>>>>>");
                        mInterval = tInterval * 1000;
                        startRepeatingTask();
                        record = true;
                    }
                }
            });
        }

        initListRecords();
        mRecordManager = new RecordManager(getApplicationContext());

        mHandler = new Handler();

    }

    private Runnable mDataCollection = new Runnable() {
        @Override
        public void run() {
            try {
                checkPermissionUser();
            } finally {
                mHandler.postDelayed(mDataCollection, mInterval);
            }
        }
    };

    private void startRepeatingTask() {
        mDataCollection.run();
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(mDataCollection);
    }

    public void startRecord(View view) {
        long tInterval = PreferenceUtils.getPrefLong(this, "time_interval", 5);
        Log.e("聊啥呢", tInterval + ">>>>>>>>>>>>>>>>>>>>>>");
        mInterval = tInterval * 1000;
        startRepeatingTask();
    }

    public void stopRecord(View view) {
        stopRepeatingTask();
    }

    public void exportJson(View view) {

        /*Gson gson = new GsonBuilder().registerTypeAdapter(Record.class, new RecordSerializer()).setPrettyPrinting().create();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Record> results = realm.where(Record.class).findAll();
        for (Record r : results) {
            String json = gson.toJson(r);
            Log.e("廖山河", json);
        }*/
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
