package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.FuncN;

/**
 * Created by remirobert on 19/07/16.
 */

//http://opencellid.org/cell/get?key=e5dad4a2-e436-412c-8178-064b8fef2ecc&mcc=208&mnc=15&lac=3300&cellid=104187198&format=json

public class CellTowerManager {

    private static final String TAG = "CellTowerProximityInfo";

    private TelephonyManager mTelephonyManager;
    private Context mContext;

    public CellTowerManager(Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private CellularTower checkInvalidData(CellularTower cellularTower) {
        if (cellularTower == null) {
            return null;
        }
        if (cellularTower.getMcc() != Integer.MAX_VALUE &&
                cellularTower.getMnc() != Integer.MAX_VALUE &&
                cellularTower.getCid() != Integer.MAX_VALUE &&
                cellularTower.getLac() != Integer.MAX_VALUE) {
            return cellularTower;
        }
        return null;
    }

    private CellularTower bindData(CellInfo cellInfo) {
        CellularTower cellularTower = null;
        Log.v(TAG, cellInfo.toString());
        Log.v(TAG, "" + Integer.MAX_VALUE);

        if (cellInfo instanceof CellInfoWcdma) {
            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
            CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
            cellularTower = new CellularTower();
            cellularTower.setType("WCDMA");
            cellularTower.setCid(cellIdentityWcdma.getCid());
            cellularTower.setLac(cellIdentityWcdma.getLac());
            cellularTower.setMcc(cellIdentityWcdma.getMcc());
            cellularTower.setMnc(cellIdentityWcdma.getMnc());
            cellularTower.setPsc_or_pci(cellIdentityWcdma.getPsc());
            if (cellInfoWcdma.getCellSignalStrength() != null) {
                cellularTower.setAsuLevel(cellInfoWcdma.getCellSignalStrength().getAsuLevel()); //Get the signal level as an asu value between 0..31, 99 is unknown Asu is calculated based on 3GPP RSRP.
                cellularTower.setSignalLevel(cellInfoWcdma.getCellSignalStrength().getLevel()); //Get signal level as an int from 0..4
                cellularTower.setSignalDbm(cellInfoWcdma.getCellSignalStrength().getDbm()); //Get the signal strength as dBm
            }
        } else if (cellInfo instanceof CellInfoLte) {
            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
            CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
            cellularTower = new CellularTower();
            cellularTower.setType("LTE");
            cellularTower.setCid(cellIdentityLte.getCi());
            cellularTower.setMnc(cellIdentityLte.getMnc());
            cellularTower.setMcc(cellIdentityLte.getMcc());
            cellularTower.setLac(cellIdentityLte.getTac());
            cellularTower.setPsc_or_pci(cellIdentityLte.getPci());
            if (cellInfoLte.getCellSignalStrength() != null) {
                cellularTower.setAsuLevel(cellInfoLte.getCellSignalStrength().getAsuLevel());
                cellularTower.setSignalLevel(cellInfoLte.getCellSignalStrength().getLevel());
                cellularTower.setSignalDbm(cellInfoLte.getCellSignalStrength().getDbm());
            }
        } else if (cellInfo instanceof CellInfoGsm) {
            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
            CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
            cellularTower = new CellularTower();
            cellularTower.setType("GSM");
            cellularTower.setCid(cellIdentityGsm.getCid());
            cellularTower.setLac(cellIdentityGsm.getLac());
            cellularTower.setMcc(cellIdentityGsm.getMcc());
            cellularTower.setMnc(cellIdentityGsm.getMnc());
            cellularTower.setPsc_or_pci(cellIdentityGsm.getPsc());
            if (cellInfoGsm.getCellSignalStrength() != null) {
                cellularTower.setAsuLevel(cellInfoGsm.getCellSignalStrength().getAsuLevel());
                cellularTower.setSignalLevel(cellInfoGsm.getCellSignalStrength().getLevel());
                cellularTower.setSignalDbm(cellInfoGsm.getCellSignalStrength().getDbm());
            }
        } else {
            Log.e(TAG, "CDMA CellInfo................................................");
        }
        return cellularTower;
    }

    public CellularTower getConnectedTower() {
        String operator = mTelephonyManager.getNetworkOperator();
        int mcc, mnc;
        try {
            mcc = Integer.parseInt(operator.substring(0, 3));
        } catch (Exception e) {
            mcc = 0;
        }
        try {
            mnc = Integer.parseInt(operator.substring(3));
        } catch (Exception e) {
            mnc = 0;
        }
        CellLocation cellLocation = mTelephonyManager.getCellLocation();
        CellularTower tower = new CellularTower();
        tower.setMcc(mcc);
        tower.setMnc(mnc);

        if (cellLocation instanceof GsmCellLocation) {
            tower.setPsc_or_pci(((GsmCellLocation) cellLocation).getPsc());
            tower.setCid(((GsmCellLocation) cellLocation).getCid());
            tower.setLac(((GsmCellLocation) cellLocation).getLac());
            tower.setType("");
            Log.v(TAG, "Get connected tower GSM");
            Log.v(TAG, cellLocation.toString());
        }
        return tower;
    }

    public List<CellularTower> getTowerList() {
        List<CellInfo> cellInfoList = null;
        List<CellularTower> cellularTowerList = new ArrayList<>();

        cellInfoList = mTelephonyManager.getAllCellInfo();
        if (cellInfoList == null || cellInfoList.size() == 0) {
            if (cellInfoList == null) {
                Log.v(TAG, "getAllCellInfo() returns null");
            } else {
                Log.v(TAG, "getAllCellInfo() list size 0");
            }
            return cellularTowerList;
        }

        for (int i = 0; i < cellInfoList.size(); i++) {
            final CellInfo cellInfo = cellInfoList.get(i);
            final CellularTower cellularTower = bindData(cellInfo);
            if (cellularTower != null) {
                cellularTowerList.add(cellularTower);
            }
        }
        return cellularTowerList;
    }

    private rx.Observable<CellularTower> locationTower(final CellularTower cellularTower) {
        return rx.Observable.create(new Observable.OnSubscribe<CellularTower>() {
            @Override
            public void call(final Subscriber<? super CellularTower> subscriber) {
                ServiceGenerator.changeApiBaseUrl("http://opencellid.org");
                final CellIdClient cellIdClient = ServiceGenerator.createService(CellIdClient.class);
                Call<CellIdResponse> cellIdResponseCall = cellIdClient.cellInformations("1085e718-c5a6-4392-9062-e57527c7bd97",
                        cellularTower.getMcc(),
                        cellularTower.getMnc(),
                        cellularTower.getLac(),
                        cellularTower.getCid(),
                        "json");

                cellIdResponseCall.enqueue(new Callback<CellIdResponse>() {
                    @Override
                    public void onResponse(Call<CellIdResponse> call, Response<CellIdResponse> response) {
                        Log.v(TAG, "request : " + call.request().toString());
                        Log.v(TAG, "response request : " + response.body());
                        Log.v(TAG, "response errorBody : " + response.errorBody());

                        CellIdResponse cellIdResponse = response.body();

                        if (cellIdResponse != null) {
                            cellularTower.setLat(cellIdResponse.getLat());
                            cellularTower.setLon(cellIdResponse.getLon());
                        }

                        subscriber.onNext(cellularTower);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<CellIdResponse> call, Throwable t) {
                        Log.e(TAG, call.request().toString());
                        Log.e(TAG, t.toString());
//                        subscriber.onError(t);
                        subscriber.onNext(cellularTower);
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    public rx.Observable<List<CellularTower>> nerbyTower() {
        return rx.Observable.create(new Observable.OnSubscribe<List<CellularTower>>() {
            @Override
            public void call(final Subscriber<? super List<CellularTower>> subscriber) {
                final List<CellularTower> cellularTowerList = getTowerList();
                List<Observable<CellularTower>> observableList = new ArrayList<>();

                if (cellularTowerList.size() == 0) {
                    subscriber.onNext(cellularTowerList);
                    subscriber.onCompleted();
                }

                for (CellularTower tower : cellularTowerList) {
                    Observable<CellularTower> observable = locationTower(tower);
                    observableList.add(observable);
                }

                rx.Observable.zip(observableList, new FuncN<List<CellularTower>>() {
                    @Override
                    public List<CellularTower> call(Object... args) {
                        List<CellularTower> listResponse = new ArrayList<>();

                        for (int i = 0; i < args.length; i++) {
                            listResponse.add((CellularTower) args[0]);
                        }
                        Log.v(TAG, "Request finished length data : " + args.length);
                        Log.v(TAG, "Get list response : " + listResponse.size());
                        return listResponse;
                    }
                }).subscribe(new Subscriber<List<CellularTower>>() {
                    @Override
                    public void onCompleted() {
                        Log.v(TAG, "Completed all services API");
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "get error : " + e.toString());
                    }

                    @Override
                    public void onNext(List<CellularTower> towerList) {
                        Realm realm = Realm.getDefaultInstance();

                        realm.beginTransaction();
                        realm.copyToRealm(towerList);
                        realm.commitTransaction();

                        Log.v(TAG, "Get list response cell Id : " + towerList.size());
                        subscriber.onNext(cellularTowerList);
                    }
                });
            }
        });
    }
}
