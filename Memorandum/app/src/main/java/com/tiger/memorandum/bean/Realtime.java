/**
  * Copyright 2021 bejson.com 
  */
package com.tiger.memorandum.bean;

/**
 * Auto-generated: 2021-04-12 14:11:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Realtime {

    private float temperature;

    private String skycon;


    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getSkycon() {
        return skycon;
    }

    @Override
    public String toString() {
        return "Realtime{" +
                "temperature=" + temperature +
                ", skycon='" + skycon + '\'' +
                '}';
    }

    public void setSkycon(String skycon) {
        this.skycon = skycon;
    }
}