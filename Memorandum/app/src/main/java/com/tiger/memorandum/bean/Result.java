/**
  * Copyright 2021 bejson.com 
  */
package com.tiger.memorandum.bean;


public class Result {

    private Realtime realtime;
    private int primary;
    public void setRealtime(Realtime realtime) {
         this.realtime = realtime;
     }
     public Realtime getRealtime() {
         return realtime;
     }

    public void setPrimary(int primary) {
         this.primary = primary;
     }
     public int getPrimary() {
         return primary;
     }

    @Override
    public String toString() {
        return "Result{" +
                "realtime=" + realtime +
                ", primary=" + primary +
                '}';
    }
}