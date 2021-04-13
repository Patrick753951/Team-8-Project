package com.tiger.memorandum;


import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;




public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    final  static String TAG="jcy-MainActivity";
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private List<Fragment> listFragment=new ArrayList<>();
    private LocalManager mLocalManager;
    private Fragment curFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.EXPAND_STATUS_BAR})
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.e(TAG, "accept: " + aBoolean);
                        if (aBoolean) {
                            init();
                        }
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocalManager != null) {
            mLocalManager.checkGpsServerOpen(this);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocalManager != null) {
            mLocalManager.unRegister();
            mLocalManager = null;
        }
    }


    private GpsLocationListener mGpsLocationListener = new GpsLocationListener() {
        @SuppressLint("WrongConstant")
        @Override
        public void onLocationListener(double latitude, double longitude, float accuracy) {
            if(curFragment!=null&&curFragment instanceof  SecondFragment){
                ((SecondFragment) curFragment).updateWeather(latitude,longitude);
            }
        }


    };
    private void init(){
        if (mLocalManager == null) {
            mLocalManager = new LocalManager(this, 1 * 1000,  mGpsLocationListener);
            mLocalManager.register();
        }
        fragmentManager=getSupportFragmentManager();
        listFragment.add(new FirstFragment());
        listFragment.add(new SecondFragment());

        viewPager=(ViewPager)findViewById(R.id.vp_main);
        viewPager.setAdapter(new MyFragmentAdapter(fragmentManager));
        viewPager.addOnPageChangeListener(this);
        radioGroup=(RadioGroup)findViewById(R.id.rg_mainbar);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.rb_first);
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rb_first:
                Log.i(TAG,"rb_first checked");
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_second:
                Log.i(TAG,"rb_second checked");
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                radioGroup.check(R.id.rb_first);
                break;
            case 1:
                radioGroup.check(R.id.rb_second);
                break;
        }
        curFragment=listFragment.get(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }
    }
}

