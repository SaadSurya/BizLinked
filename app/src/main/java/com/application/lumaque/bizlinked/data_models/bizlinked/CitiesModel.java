package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class CitiesModel implements Serializable {


    String CityID;
    String CityName;
    String CountryID;

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCountryID() {
        return CountryID;
    }

    public void setCountryID(String countryID) {
        CountryID = countryID;
    }
}
