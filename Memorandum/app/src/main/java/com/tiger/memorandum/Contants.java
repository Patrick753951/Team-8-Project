package com.tiger.memorandum;

import com.tiger.memorandum.bean.Sky;

import java.util.HashMap;

/**
 * @name: Contants
 * @date: 2021-04-12 13:22
 * @comment:
 */
class Contants {
    public static double LATITUDE=0;
    public static double  LONGITUDE=0;

    public static HashMap<String, Sky> mSkyMap = new HashMap<>();

    public static void  initSkyInfo(){
        mSkyMap.clear();
        mSkyMap.put("CLEAR_DAY",new Sky("晴", R.drawable.ic_clear_day));
        mSkyMap.put( "CLEAR_NIGHT",new Sky("晴", R.drawable.ic_clear_night));
        mSkyMap.put("PARTLY_CLOUDY_DAY",new Sky("多云", R.drawable.ic_partly_cloud_day));
        mSkyMap.put( "PARTLY_CLOUDY_NIGHT",new Sky("多云", R.drawable.ic_partly_cloud_night));
        mSkyMap.put( "CLOUDY",new Sky("阴", R.drawable.ic_cloudy));
        mSkyMap.put("WIND",new Sky("大风", R.drawable.ic_cloudy));
        mSkyMap.put("LIGHT_RAIN",new Sky("小雨", R.drawable.ic_light_rain));
        mSkyMap.put( "MODERATE_RAIN",new Sky("中雨", R.drawable.ic_moderate_rain));
        mSkyMap.put("HEAVY_RAIN",new Sky("大雨", R.drawable.ic_heavy_rain));
        mSkyMap.put("STORM_RAIN",new Sky("暴雨", R.drawable.ic_storm_rain));
        mSkyMap.put("THUNDER_SHOWER",new Sky("雷阵雨", R.drawable.ic_thunder_shower));
        mSkyMap.put("SLEET",new Sky("雨夹雪", R.drawable.ic_sleet));
        mSkyMap.put("LIGHT_SNOW",new Sky("小雪", R.drawable.ic_light_snow));
        mSkyMap.put("MODERATE_SNOW",new Sky("中雪", R.drawable.ic_moderate_snow));
        mSkyMap.put( "HEAVY_SNOW",new Sky("大雪", R.drawable.ic_heavy_snow));
        mSkyMap.put("STORM_SNOW",new Sky("暴雪", R.drawable.ic_heavy_snow));
        mSkyMap.put("HAIL",new Sky("冰雹", R.drawable.ic_hail));
        mSkyMap.put("LIGHT_HAZE",new Sky("轻度雾霾", R.drawable.ic_light_haze));
        mSkyMap.put("MODERATE_HAZE",new Sky("中度雾霾", R.drawable.ic_moderate_haze));
        mSkyMap.put("HEAVY_HAZE",new Sky("重度雾霾", R.drawable.ic_heavy_haze));
        mSkyMap.put( "FOG",new Sky("雾", R.drawable.ic_fog));
        mSkyMap.put( "DUST",new Sky("浮尘", R.drawable.ic_fog));
    }

}
