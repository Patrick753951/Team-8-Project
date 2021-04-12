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

/**
 * @name: LocalManager
 * @author: jiangcy
 * @email: jcy0177@woozoom.net
 * @date: 2017-11-18 10:44
 * @comment:
 */
public class LocalManager {
    private static String TAG = LocalManager.class.getSimpleName();
    private RxPermissions rxPermissions = null;//权限管理
    //    private Activity activity;
    private WeakReference<Activity> mActivityWeakReference;
    private LocationManager locationManager;//获取位置信息
    private int time_interval = 1000; // 0.5 second
    private Disposable mDisposable;
    private boolean isGpsOutService = true;
    private Location mLocation = null;
    private GpsLocationListener mGpsLocationListener;
    private int gpsNum = 0;
    private final String LOCALLATITUDE = "LOCALLATITUDE";
    private final String LOCALLONGITUDE = "LOCALLONGITUDE";
    private Handler mHandler;
    private final int GPS_LOSE = 0x1001;
    private HandlerThread mHandlerThread;//HandlerThread能够新建拥有Looper的线程,必然是执行耗时操作(数据实时更新，我们每10秒需要切换一下显示的数据)
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
                .setMessage("未开启GPS无法使用，请立即设置")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
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

    /**
     * 注册
     */
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

        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
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

    /**
     * 解除注册
     */
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


    //监测定位权限
    public boolean checkLocationPermission() {
        if (rxPermissions == null) {
            return false;
        }
//
        return rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION);

    }


    // 获取Location Provider
    private String getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(false);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 是否允许付费：是
        criteria.setCostAllowed(true);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
        return locationManager.getBestProvider(criteria, true);
    }

    // GPS位置监听
    private LocationListener mGpslocListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
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

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    //  Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    //Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    // Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
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

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
        }

    };

}
