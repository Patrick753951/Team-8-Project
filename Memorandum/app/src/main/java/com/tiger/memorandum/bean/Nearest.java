/**
  * Copyright 2021 bejson.com 
  */
package com.tiger.memorandum.bean;


public class Nearest {

    private String status;
    private double distance;
    private double intensity;
    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setDistance(double distance) {
         this.distance = distance;
     }
     public double getDistance() {
         return distance;
     }

    public void setIntensity(double intensity) {
         this.intensity = intensity;
     }
     public double getIntensity() {
         return intensity;
     }

}