package com.remirobert.remirobert.signalstrentgh;

import android.content.Context;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by remirobert on 14/07/16.
 */
public class SignalObserver extends PhoneStateListener {

    private static final String TAG = "SignalObserver";

    private Context mContext;
    private TelephonyManager mTelephonyManager;

    private Subscriber<? super SignalRecord> mSubscriber;

    private void signalMeasuring(SignalStrength signalStrength) {
        String ssignal = signalStrength.toString();
        String[] parts = ssignal.split(" ");

        Log.v(TAG, ssignal);

        int dB = -120;
        if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            int ltesignal = Integer.parseInt(parts[9]);
            if (ltesignal < -2) {
                dB = ltesignal;
            }
        } else {
            if (signalStrength.getGsmSignalStrength() != 99) {
                int strengthInteger = -113 + 2 * signalStrength.getGsmSignalStrength();
                dB = strengthInteger;
            }
        }

        SignalRecord signalRecord = new SignalRecord();
        signalRecord.setDb(dB);
        signalRecord.setGsm(signalStrength.isGsm());
        signalRecord.setNoise(signalStrength.getEvdoSnr());
        signalRecord.setEvdoEci(signalStrength.getEvdoEcio());
        signalRecord.setLevel(signalStrength.getLevel());
        signalRecord.setStatutSignal((dB == -120) ? SignalRecord.SIGNAL_STATUT_NONE : SignalRecord.SIGNAL_STATUT_OK);

        mSubscriber.onNext(signalRecord);
        mSubscriber.onCompleted();
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        signalMeasuring(signalStrength);
    }

    public SignalObserver(Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
    }

    private void startListening() {
        mTelephonyManager.listen(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private void stopListening() {
        mTelephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
    }

    public rx.Observable<SignalRecord> observeOnce() {
        return observe().flatMap(new Func1<SignalRecord, rx.Observable<SignalRecord>>() {
            @Override
            public rx.Observable<SignalRecord> call(SignalRecord signalRecord) {
                stopListening();
                return rx.Observable.just(signalRecord);
            }
        });
    }

    public rx.Observable<SignalRecord> observe() {
        return rx.Observable.create(new rx.Observable.OnSubscribe<SignalRecord>() {
            @Override
            public void call(Subscriber<? super SignalRecord> subscriber) {
                mSubscriber = subscriber;
                startListening();
            }
        });
    }
}
