package com.tiger.memorandum;

/**
 * @name: GpsLocationListener
 * @date: 2021-04-12 13:17
 * @comment:
 */
public interface GpsLocationListener {
    /**
     * @param latitude  维度
     * @param longitude 经度
     * @param accuracy  水平精度
     */
    void onLocationListener(double latitude, double longitude, float accuracy);
}
