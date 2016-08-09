package com.remirobert.remirobert.signalstrentgh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
                JRecord jRecord = new JRecord(record);
                final RecordClient recordClient = ServiceGenerator.createService(RecordClient.class);
                recordClient.postNewRecord(jRecord).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.v(TAG, "request success");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
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
        } else if (id == R.id.action_export) {
            exportData();
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveJson(String json) {
        if (isExternalStorageWritable()) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getString(R.string.export_data_dir_name));
            if (!dir.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }
            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String filename = getString(R.string.export_data_file_name_prefix) + "_" + sdf.format(date) + ".json";
                File file = new File(dir, filename);
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        Log.e(TAG, "File not created");
                    }
                }
                FileWriter fileWriter = new FileWriter(file.getAbsolutePath(), false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(json);
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Saving data error");
            }
        } else {
            Toast.makeText(this, "ExternalStorage unavailable", Toast.LENGTH_LONG).show();
        }
    }

    public void exportData() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Record> results = realm.where(Record.class).findAll();

        rx.Observable.create(new rx.Observable.OnSubscribe<List<JRecord>>() {
            @Override
            public void call(Subscriber<? super List<JRecord>> subscriber) {
                List<JRecord> jRecords = new ArrayList<>();
                for (Record r : results) {
                    JRecord myRecord = new JRecord(r);
                    jRecords.add(myRecord);
                }
                subscriber.onNext(jRecords);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<List<JRecord>>() {
                    @Override
                    public void onCompleted() {
                        Log.v(TAG, "export data successfully.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "export data failed.");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<JRecord> jRecords) {
                        Gson gson = new Gson();
                        String json = gson.toJson(jRecords);
                        saveJson(json);
                    }
                });
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
