package com.tiger.memorandum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tiger.memorandum.bean.Sky;
import com.tiger.memorandum.bean.WeatherBean;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SecondFragment extends Fragment implements DatePicker.OnDateChangedListener {
    private final String TAG = "SecondFragment";
    private View view;
    private DatePicker datePicker;
    private int year;
    private int month;
    private int day;
    private boolean isGetSkyInfo = false;
    private TextView tv_tmp;
    private ImageView iv_sky;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_fragment, container, false);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        tv_tmp = view.findViewById(R.id.tv_tmp);
        iv_sky = view.findViewById(R.id.iv_sky);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Contants.initSkyInfo();
        datePicker.init(year, month, day, this);
        return view;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
        year = y;
        month = m;
        day = d;
        Intent intent = new Intent(getActivity(), InfoActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day + 1);
        startActivity(intent);
    }

    public void updateWeather(double latitude, double longitude) {
        if(latitude==0||longitude==0){
            return;
        }
        if (!isGetSkyInfo) {
            isGetSkyInfo = true;
            String url = "https://api.caiyunapp.com/v2.5/j7Bo8aHef0f8FN9J/" + longitude + "," + latitude + "/realtime.json";
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()//默认就是GET请求，可以不写
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: ");
                    isGetSkyInfo = false;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    Log.d(TAG, "onResponse: " + body);
                    Gson gson = new Gson();
                    final WeatherBean weather = gson.fromJson(body, WeatherBean.class);
                    Log.d(TAG, "WeatherBean: " + weather);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Sky sky = Contants.mSkyMap.get(weather.getResult().getRealtime().getSkycon());
                            if(sky!=null){
                                tv_tmp.setText(weather.getResult().getRealtime().getTemperature() + "℃ "+sky.info);
                                iv_sky.setImageResource(sky.icon);

                            }else {
                                tv_tmp.setText(weather.getResult().getRealtime().getTemperature() + "℃"+weather.getResult().getRealtime().getTemperature() + "℃");

                            }
                        }
                    });
                }
            });
        }
    }
}