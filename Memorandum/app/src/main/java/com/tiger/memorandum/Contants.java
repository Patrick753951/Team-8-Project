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
        mSkyMap.put("CLEAR_DAY",new Sky("Sunny", R.drawable.ic_clear_day));
        mSkyMap.put( "CLEAR_NIGHT",new Sky("Sunny", R.drawable.ic_clear_night));
        mSkyMap.put("PARTLY_CLOUDY_DAY",new Sky("Cloudy", R.drawable.ic_partly_cloud_day));
        mSkyMap.put( "PARTLY_CLOUDY_NIGHT",new Sky("Cloudy", R.drawable.ic_partly_cloud_night));
        mSkyMap.put( "CLOUDY",new Sky("Cloudy", R.drawable.ic_cloudy));
        mSkyMap.put("WIND",new Sky("gale", R.drawable.ic_cloudy));
        mSkyMap.put("LIGHT_RAIN",new Sky("drizzle", R.drawable.ic_light_rain));
        mSkyMap.put( "MODERATE_RAIN",new Sky("moderate rain", R.drawable.ic_moderate_rain));
        mSkyMap.put("HEAVY_RAIN",new Sky("heavy rain", R.drawable.ic_heavy_rain));
        mSkyMap.put("STORM_RAIN",new Sky("downpour", R.drawable.ic_storm_rain));
        mSkyMap.put("THUNDER_SHOWER",new Sky("thunder shower", R.drawable.ic_thunder_shower));
        mSkyMap.put("SLEET",new Sky("sleet", R.drawable.ic_sleet));
        mSkyMap.put("LIGHT_SNOW",new Sky("Light Snow", R.drawable.ic_light_snow));
        mSkyMap.put("MODERATE_SNOW",new Sky("moderate snow", R.drawable.ic_moderate_snow));
        mSkyMap.put( "HEAVY_SNOW",new Sky("heavy snow", R.drawable.ic_heavy_snow));
        mSkyMap.put("STORM_SNOW",new Sky("storm", R.drawable.ic_heavy_snow));
        mSkyMap.put("HAIL",new Sky("hail", R.drawable.ic_hail));
        mSkyMap.put("LIGHT_HAZE",new Sky("Mild haze", R.drawable.ic_light_haze));
        mSkyMap.put("MODERATE_HAZE",new Sky("Moderate haze", R.drawable.ic_moderate_haze));
        mSkyMap.put("HEAVY_HAZE",new Sky("Heavy haze", R.drawable.ic_heavy_haze));
        mSkyMap.put( "FOG",new Sky("fog", R.drawable.ic_fog));
        mSkyMap.put( "DUST",new Sky("floating dust", R.drawable.ic_fog));
    }

}
