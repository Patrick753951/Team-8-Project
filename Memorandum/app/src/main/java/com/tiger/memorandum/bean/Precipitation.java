package com.tiger.memorandum.bean;

public class Precipitation {

    private Local local;
    private Nearest nearest;
    public void setLocal(Local local) {
         this.local = local;
     }
     public Local getLocal() {
         return local;
     }

    public void setNearest(Nearest nearest) {
         this.nearest = nearest;
     }
     public Nearest getNearest() {
         return nearest;
     }

}
