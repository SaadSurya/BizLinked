package com.application.lumaque.bizlinked.helpers.placeHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.listener.GooglePlaceDataInterface;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;


public class GooglePlaceHelper {


    public static final int REQUEST_CODE_AUTOCOMPLETE = 6666;
    public static final int PLACE_PICKER = 0;
    public static final int PLACE_AUTOCOMPLETE = 1;
    private static final String TAG = "Google Place";
    private int apiType;
    private GooglePlaceDataInterface googlePlaceDataInterface;
    private Activity context;
    private Fragment fragment;

    /**
     * @param context
     * @param apiType                  Api Type can be PLACE_PICKER or PLACE_AUTOCOMPLETE
     * @param googlePlaceDataInterface implement this interface in your fragment then pass its instance as this.
     * @param fragment                 your current fragment
     */

    public GooglePlaceHelper(Activity context, int apiType, GooglePlaceDataInterface googlePlaceDataInterface, Fragment fragment) {
        this.context = (context);
        this.apiType = apiType;
        this.googlePlaceDataInterface = googlePlaceDataInterface;
        this.fragment = fragment;
    }
    //

    /**
     * GET ADDRESS from Geo Coder.
     *
     * @param context
     * @param LATITUDE
     * @param LONGITUDE
     * @return
     */

    public static GoogleAddressModel getAddress(Context context, double LATITUDE, double LONGITUDE) {

        GoogleAddressModel googleAddressModel = new GoogleAddressModel("", "", "", "", "", "");

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String streetName = addresses.get(0).getFeatureName(); // Only if available else return NULL


                googleAddressModel = new GoogleAddressModel(address, city, state, country, postalCode, streetName);

                Log.d(TAG, "getAddress:  address -" + address);
                Log.d(TAG, "getAddress:  city -" + city);
                Log.d(TAG, "getAddress:  country -" + country);
                Log.d(TAG, "getAddress:  state -" + state);
                Log.d(TAG, "getAddress:  postalCode -" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + streetName);


                String countryCode = addresses.get(0).getCountryCode();
                Log.d(TAG, "getAddress:  countryCode" + countryCode);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleAddressModel;
    }

    /**
     * Call this method in a fragment when you want to open the map
     * CHOOSE MODE_FULLSCREEN OR OVERLAY for Popup
     */

    public void openAutocompleteActivity() {
        try {


            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent;


            if (apiType == PLACE_PICKER) {
                intent = new PlacePicker.IntentBuilder()
                        .build(context);
            } else {
                intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(context);
            }

            context.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            //fragment.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(context, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Utils.showToast(context, message, AppConstant.TOAST_TYPES.ERROR);
        }
    }

    /**
     * Override fragment's onActivityResult and pass its parameters here.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GooglePlaceHelper.REQUEST_CODE_AUTOCOMPLETE && data != null) {

            if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                if (PlacePicker.getStatus(context, data) == null) {
                    return;
                }
                Status status = PlaceAutocomplete.getStatus(context, data);
                Log.e(TAG, "Error: Status = " + status.toString());
                googlePlaceDataInterface.onError(status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            } else {


                if (PlacePicker.getPlace(context, data) == null) {
                    return;
                }


                Place place = PlacePicker.getPlace(context, data);

                String address = place.getAddress().toString();
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;


                Log.d(TAG, "onActivityResult MAP: locationName = " + address);
                Log.d(TAG, "onActivityResult MAP: latitude = " + latitude);
                Log.d(TAG, "onActivityResult MAP: longitude = " + longitude);

//                Log.d(TAG, "onActivityResult MAP: Locale = " +    place.getLocale().toString());
//                Log.d(TAG, "onActivityResult MAP: Country = " +    place.getLocale().getCountry());
//                Log.d(TAG, "onActivityResult MAP: Display Country = " + place.getLocale().getDisplayCountry());
//                Log.d(TAG, "onActivityResult MAP: Display Name = " + place.getLocale().getDisplayName());

                googlePlaceDataInterface.onPlaceActivityResult(longitude, latitude, place);


            }

        }
    }

    public static class GoogleAddressModel {
        private String address;
        private String city;
        private String state;
        private String country;
        private String postalCode;
        private String streetName;


        public GoogleAddressModel(String address, String city, String state, String country, String postalCode, String streetName) {
            this.address = address;
            this.city = city;
            this.state = state;
            this.country = country;
            this.postalCode = postalCode;
            this.streetName = streetName;
        }


        public String getAddress() {
            return address == null ? "" : address;
        }

        public String getCity() {
            return city == null ? "" : city;
        }

        public String getState() {
            return state == null ? "" : state;
        }

        public String getCountry() {
            return country == null ? "" : country;
        }

        public String getPostalCode() {
            return postalCode == null ? "" : postalCode;
        }

        public String getStreetName() {
            return streetName == null ? "" : streetName;
        }
    }


}