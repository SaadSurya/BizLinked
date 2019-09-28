package com.application.lumaque.bizlinked.data_models;


public class PlacesModel {

    private String hospCode;
    private double lat;
    private double lng;
    private String name;
    private double rate;
    private double destFrom;


    public PlacesModel() {
    }

    public PlacesModel(String hospCode, double lat, double lng, String name, double rate, double destFrom) {
        this.hospCode = hospCode;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.rate = rate;
        this.destFrom = destFrom;
    }


    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

    public double getDestFrom() {
        return destFrom;
    }

    public void setDestFrom(double destFrom) {
        this.destFrom = destFrom;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }


}
