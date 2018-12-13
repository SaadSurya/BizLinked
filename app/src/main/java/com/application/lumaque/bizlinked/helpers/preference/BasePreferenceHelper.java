package com.application.lumaque.bizlinked.helpers.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;
    protected static final String KEY_LOGIN_STATUS = "is_login";
    protected static final String KEY_USER = "user";
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String AUTHENTICATE_USER_TOKEN = "user_token";
    private static final String FILENAME = "bizlinked_preferences";
    public static final String KEY_CATEGORY = "category";


    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean isLogin) {
        putBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS, isLogin);
    }


    public void setStringPrefrence(String key, String value) {
        putStringPreference(context, FILENAME, key, value);
    }

    public String getStringPrefrence(String key) {
        return getStringPreference(context, FILENAME, key);
    }


    public void setIntegerPrefrence(String key, int value) {
        putIntegerPreference(context, FILENAME, key, value);
    }

    public int getIntegerPrefrence(String key) {
        return getIntegerPreference(context, FILENAME, key);
    }


    public void setBooleanPrefrence(String Key, boolean value) {
        putBooleanPreference(context, FILENAME, Key, value);
    }

    public boolean getBooleanPrefrence(String Key) {
        return getBooleanPreference(context, FILENAME, Key);
    }


    public boolean getLoginStatus() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }

    public void putDeviceToken(String token) {
        putStringPreference(context, FILENAME, KEY_DEVICE_TOKEN, token);
    }


    public String getDeviceToken() {
        return getStringPreference(context, FILENAME, KEY_DEVICE_TOKEN);
    }


    public void putUserToken(String token) {
        putStringPreference(context, FILENAME, AUTHENTICATE_USER_TOKEN, token);
    }


    public String getUserToken() {
        return getStringPreference(context, FILENAME, AUTHENTICATE_USER_TOKEN);
    }


    public void putCompany(CompanyProfileModel companyprofile) {
        putStringPreference(context,
                FILENAME,
                KEY_USER,
                new GsonBuilder()
                        .create()
                        .toJson(companyprofile));
    }

    public CompanyProfileModel getCompanyProfile() {
        return new GsonBuilder().create().fromJson(
                getStringPreference(context, FILENAME, KEY_USER), CompanyProfileModel.class);
    }



    public void putCategory(String categoryArray) {
        putStringPreference(context,
                FILENAME,
                KEY_CATEGORY,
                categoryArray);
    }

    public ArrayList<ProductCategory> getCategoryList() {


        Type listType = new TypeToken<List<ProductCategory>>() {
        }.getType();


        return new GsonBuilder().create().fromJson(
                getStringPreference(context, FILENAME, KEY_CATEGORY),listType);
    }

    public void removeLoginPreference() {
        setLoginStatus(false);
        removePreference(context, FILENAME, KEY_USER);
        removePreference(context, FILENAME, KEY_LOGIN_STATUS);
        removePreference(context, FILENAME, AUTHENTICATE_USER_TOKEN);

    }


}
