package com.example.muszaki;

public class Model {
    String nev, ar, kategoria, url;
    Model() {}
    public Model(String nev, String ar, String kategoria, String url) {
        this.nev = nev;
        this.ar = ar;
        this.kategoria = kategoria;
        this.url = url;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
