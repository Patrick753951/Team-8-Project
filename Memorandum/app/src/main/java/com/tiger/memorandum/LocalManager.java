package com.tiger.memorandum;

/**
 * @name: LocalManager
 * @date: 2021-04-12 13:18
 * @comment:
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LocalManager {
    private static String TAG = LocalManager.class.getSimpleName();
    private RxPermissions rxPermissions = null;
    private WeakReference<Activity> mActivityWeakReference;
    private LocationManager locationManager;
    private int time_interval = 1000;
    private Disposable mDisposable;
    private boolean isGpsOutService = true;
    private Location mLocation = null;
    private GpsLocationListener mGpsLocationListener;
    private int gpsNum = 0;
    private final String LOCALLATITUDE = "LOCALLATITUDE";
    private final String LOCALLONGITUDE = "LOCALLONGITUDE";
    private Handler mHandler;
    private final int GPS_LOSE = 0x1001;
    private HandlerThread mHandlerThread;
    private Disposable mTimerDisposable;

    public LocalManager(Activity activity, int time_interval, GpsLocationListener gpsLocationListener) {
        init(activity, time_interval,  gpsLocationListener);
    }

    public LocalManager(Activity activity, GpsLocationListener gpsLocationListener) {
        init(activity, time_interval,  gpsLocationListener);
    }

    private void init(Activity activity, int time_interval,GpsLocationListener gpsLocationListener) {
        mGpsLocationListener = gpsLocationListener;
        mActivityWeakReference = new WeakReference<>(activity);
        rxPermissions = new RxPermissions(mActivityWeakReference.get());
        this.time_interval = time_interval;
        if (null != mActivityWeakReference && mActivityWeakReference.get() != null) {
            locationManager = (LocationManager) mActivityWeakReference.get().getSystemService(Context.LOCATION_SERVICE);
        }
        register();
        checkGpsServerOpen(mActivityWeakReference.get());
        isGpsOutService = true;
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("LocalManager");
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == GPS_LOSE) {
                        isGpsOutService = true;
                    }

                }
            };
        }
    }


    public void checkGpsServerOpen(Context context) {
        boolean okGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d(TAG, " checkGpsServerOpen okGps " + okGps );
        if (!okGps) {
            checkGpsServer(context);
        }

    }


    private void checkGpsServer(final Context context) {
        if (mTimerDisposable != null) {
            mTimerDisposable.dispose();
            mTimerDisposable = null;
        }
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mTimerDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        boolean okGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        Log.d(TAG, "onComplete: checkGpsServerOpen okGps " + okGps+ "  " + Thread.currentThread().getName());
                        if (!okGps) {
                            showOpenGpsDialog(context);
                        }

                    }
                });

    }

    private AlertDialog checkGpsDialog;
    private void showOpenGpsDialog(Context context) {
        if (checkGpsDialog != null && checkGpsDialog.isShowing()) {
            return;
        }
        checkGpsDialog = new AlertDialog.Builder(context)
                .setMessage("do not open GPS service，please set to open")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mActivityWeakReference.get().startActivityForResult(intent, 1315);
                    }
                })
                .create();
        checkGpsDialog.show();
    }

    @SuppressLint("MissingPermission")
    public void register() {
        if (!checkLocationPermission()) {
            rxPermissions.request(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            Log.e(TAG, "accept: " + aBoolean);
                            if (aBoolean) {
                                initLocal();
                            }
                        }
                    });
        } else {
            initLocal();
        }

    }


    @SuppressLint("MissingPermission")
    private void initLocal() {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mGpslocListener);
        mLocation = locationManager.getLastKnownLocation(getProvider());
        if (!(mLocation != null && mLocation.getLongitude() != 0 && mLocation.getLatitude() != 0) && mLocation != null) {
            if (null != mActivityWeakReference && mActivityWeakReference.get() != null) {
              Contants.LATITUDE = Double.valueOf(SPUtils.getInstance().getFloat(LOCALLATITUDE));
              Contants.LONGITUDE = Double.valueOf(SPUtils.getInstance().getFloat(LOCALLATITUDE));
            }
            mLocation.setLatitude(Contants.LATITUDE);
            mLocation.setLongitude(Contants.LONGITUDE);
        }
        Observable.interval(0, time_interval, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
//                        JLog.d(TAG, "onNext: mLocation " + mLocation + "  mGpsLocationListener " + mGpsLocationListener);
                        if (mGpsLocationListener != null) {
                            if (mLocation != null && mLocation.getLongitude() != 0 && mLocation.getLatitude() != 0) {
                                mGpsLocationListener.onLocationListener(mLocation.getLatitude(), mLocation.getLongitude(), mLocation.getAccuracy());
                            } else {
                                mGpsLocationListener.onLocationListener(Contants.LATITUDE, Contants.LONGITUDE, 0);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void unRegister() {
        if (mLocation != null) {
            if (null != mActivityWeakReference && mActivityWeakReference.get() != null) {
                SPUtils.getInstance().put(LOCALLATITUDE, (float) mLocation.getLatitude());
                SPUtils.getInstance().put(LOCALLONGITUDE, (float) mLocation.getLongitude());
            }
        }
        if (checkGpsDialog != null) {
            checkGpsDialog.dismiss();
            checkGpsDialog = null;
        }
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        if (mTimerDisposable != null) {
            mTimerDisposable.dispose();
            mTimerDisposable = null;
        }
        if (mActivityWeakReference != null) {
            mActivityWeakReference.clear();
            mActivityWeakReference = null;
        }
    }



    public boolean checkLocationPermission() {
        if (rxPermissions == null) {
            return false;
        }

        return rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION);

    }



    private String getProvider() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return locationManager.getBestProvider(criteria, true);
    }

    private LocationListener mGpslocListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            mHandler.removeMessages(GPS_LOSE);
            if (location != null) {
                isGpsOutService = false;
                mLocation = location;
                mHandler.sendEmptyMessageDelayed(GPS_LOSE, 5 * 1000);

            } else {
                mHandler.sendEmptyMessage(GPS_LOSE);
            }

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
            }
        }

        public void onProviderEnabled(String provider) {
            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null && location.getLongitude() != 0 && location.getLatitude() != 0) {
                isGpsOutService = false;
                mLocation = location;
            } else {
                isGpsOutService = true;
            }
        }

        public void onProviderDisabled(String provider) {
        }

    };

}
