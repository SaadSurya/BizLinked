package com.application.lumaque.bizlinked.data_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyArrayList {

    @SerializedName("map")
    @Expose
    private Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
