package com.remirobert.remirobert.signalstrentgh;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SignalActivity extends AppCompatActivity {

    private static final String TAG = "SignalActivity";
    private static final int REQUEST_PERMISSION = 123;
    private Handler mHandler;
    private boolean recording = false;
    private RecordManager mRecordManager;
    private Runnable mDataCollection = new Runnable() {
        @Override
        public void run() {
            try {
                checkPermissionUser2();
            } finally {
                SignalStrenght signalStrenght = (SignalStrenght) getApplication();
                mHandler.postDelayed(mDataCollection, signalStrenght.getmTimeInterval());
            }
        }
    };

    public void initListRecords() {
        Log.v(TAG, "init list records");
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Record> results = realm.where(Record.class).findAll();

        RecordAdapter recordAdapter = new RecordAdapter(results, this);
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
                        Log.e(TAG, "request fail, " + t.getMessage());
                    }
                });
                Log.v(TAG, "got record");
                initListRecords();
            }
        });
    }

    private void checkPermissionUser1() {
        Log.v(TAG, "check permission1");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            stopRecording();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            startRecording();
        }
    }

    private void checkPermissionUser2() {
        Log.v(TAG, "check permission2");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            stopRecording();
            Toast.makeText(this, "Error permissions", Toast.LENGTH_SHORT).show();
            /*ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);*/
        } else {
            requestRecord();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                stopRecording();
                Toast.makeText(this, "Error permissions", Toast.LENGTH_SHORT).show();
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
                    if (recording) {
                        stopRecording();
                    } else {
                        checkPermissionUser1();
                    }
                }
            });
        }

        initListRecords();
        mRecordManager = new RecordManager(getApplicationContext());

        mHandler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recording) {
            setTitle("SignalStrength" + "(recording...)");
        } else {
            setTitle("SignalStrength" + "(stopped)");
        }
    }

    private void startRecording() {
        mDataCollection.run();
        recording = true;
        setTitle("SignalStrength" + "(recording...)");
        TextView textView = (TextView) findViewById(R.id.fab_text);
        textView.setText("Stop");
    }

    private void stopRecording() {
        mHandler.removeCallbacks(mDataCollection);
        recording = false;
        setTitle("SignalStrength" + "(stopped)");
        TextView textView = (TextView) findViewById(R.id.fab_text);
        textView.setText("Start");
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
        } else if (id == R.id.action_export_all_data) {
            exportAllData();
        } else if (id == R.id.action_export_specified_data) {
            setTimePeriod();
        } else if (id == R.id.action_delete_data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are your sure to delete all data?");
            builder.setTitle("warning");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteData();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTimePeriod() {
        final long[] time = new long[2];
        final int[] dateTime = new int[5];

        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog date1, date2;
        final TimePickerDialog time1, time2;
        time2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.v("time2", String.format("%d, %d", hourOfDay, minute));
                dateTime[3] = hourOfDay;
                dateTime[4] = minute;
                time[1] = new GregorianCalendar(dateTime[0], dateTime[1], dateTime[2], dateTime[3], dateTime[4]).getTimeInMillis();
                exportSpecifiedData(time[0], time[1]);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        time2.setTitle("End time");

        date2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.v("date2", String.format("%d, %d, %d", year, monthOfYear, dayOfMonth));
                dateTime[0] = year;
                dateTime[1] = monthOfYear;
                dateTime[2] = dayOfMonth;
                time2.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        date2.setTitle("End date");

        time1 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.v("time1", String.format("%d, %d", hourOfDay, minute));
                dateTime[3] = hourOfDay;
                dateTime[4] = minute;
                time[0] = new GregorianCalendar(dateTime[0], dateTime[1], dateTime[2], dateTime[3], dateTime[4]).getTimeInMillis();
                date2.show();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        time1.setTitle("Start time");

        date1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.v("date1", String.format("%d, %d, %d", year, monthOfYear + 1, dayOfMonth));
                dateTime[0] = year;
                dateTime[1] = monthOfYear;
                dateTime[2] = dayOfMonth;
                time1.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        date1.setTitle("Start date");
        date1.show();
    }


    private void exportSpecifiedData(long startTime, long endTime) {
        Log.v(TAG, "startTimestamp: " + startTime + ", endTimestamp: " + endTime);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Record> results = realm.where(Record.class).between("mDate", new Date(startTime), new Date(endTime)).findAll();

        rx.Observable.create(new Observable.OnSubscribe<List<JRecord>>() {
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
                        makeExportToast(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "export data failed.");
                        makeExportToast(false);
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

    private void deleteData() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Record> results = realm.where(Record.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
        initListRecords();
    }

    public void saveJson(String json) {
        if (isExternalStorageWritable()) {
            File dir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getString(R.string.app_dir_name));
            File dir = new File(dir1, getString(R.string.export_data_dir_name));
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

    public void makeExportToast(boolean success) {
        if (success) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "export data successfully, data in /Documents/SignalStrength/ExportedData", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "export data failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void exportAllData() {
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
                        makeExportToast(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "export data failed.");
                        makeExportToast(false);
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
