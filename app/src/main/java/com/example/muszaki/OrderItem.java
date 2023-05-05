package com.example.muszaki;

public class OrderItem {
    private String ordernumber;
    private String nev;
    private String ar;

    public OrderItem(String ordernumber, String nev, String ar) {
        this.ordernumber = ordernumber;
        this.nev = nev;
        this.ar = ar;
    }

    public String getName() {
        return ordernumber;
    }
    public String getNev() {
        return nev;
    }
    public String getAr() {
        return ar;
    }
}

