package com.hfinder.entities;

import java.util.List;

/**
 * Created by root on 05/06/18.
 */

public class LogementTest {
    private long id=0L;
    private String libelle;
    private double loyer;
    private long longitude;
    private long latitude;
    private String zone;
    private List<String> imgsUrl;

    public LogementTest() {
        id=1;
        libelle = "nouveau logement";
        loyer=50000.0;
        longitude = 23;
        latitude = 10;
        zone = "ndokotti";

    }

    public LogementTest(long id, String libelle, float loyer, long longitude, long latitude, String zone) {
        this.id = id;
        this.libelle = libelle;
        this.loyer = loyer;
        this.longitude = longitude;
        this.latitude = latitude;
        this.zone = zone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getLoyer() {
        return loyer;
    }

    public void setLoyer(double loyer) {
        this.loyer = loyer;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<String> getImgsUrl() {
        return imgsUrl;
    }

    public void setImgsUrl(List<String> imgsUrl) {
        this.imgsUrl = imgsUrl;
    }
}
