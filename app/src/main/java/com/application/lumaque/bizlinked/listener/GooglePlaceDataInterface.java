package com.application.lumaque.bizlinked.listener;


import com.google.android.gms.location.places.Place;

public interface GooglePlaceDataInterface {
    void onPlaceActivityResult(double longitude, double latitude, Place place);
    void onError(String error);
}

