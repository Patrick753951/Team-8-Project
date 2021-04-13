/**
  * Copyright 2021 bejson.com 
  */
package com.tiger.memorandum.bean;


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