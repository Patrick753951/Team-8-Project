/**
  * Copyright 2021 bejson.com 
  */
package com.tiger.memorandum.bean;


public class Local {

    private String status;
    private String datasource;
    private int intensity;
    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setDatasource(String datasource) {
         this.datasource = datasource;
     }
     public String getDatasource() {
         return datasource;
     }

    public void setIntensity(int intensity) {
         this.intensity = intensity;
     }
     public int getIntensity() {
         return intensity;
     }

}