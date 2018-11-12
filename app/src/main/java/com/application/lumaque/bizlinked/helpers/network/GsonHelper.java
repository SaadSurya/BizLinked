package com.application.lumaque.bizlinked.helpers.network;


import android.content.Context;

import com.application.lumaque.bizlinked.data_models.bizlinked.BusinessNatureModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CitiesModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyHeadModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.MajorCategoryModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonHelper {


    public static ArrayList<CitiesModel> GsonToCities(Context context, String recordsArrayString) {

        Type listType = new TypeToken<List<CitiesModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static ArrayList<BusinessNatureModel> GsonToBusinessNature(Context context, String recordsArrayString) {

        Type listType = new TypeToken<List<BusinessNatureModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }
    public static ArrayList<MajorCategoryModel> GsonToMajorCategory(Context context, String recordsArrayString) {

        Type listType = new TypeToken<List<MajorCategoryModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static CompanyProfileModel GsonToCompanyProfile(Context context, String recordsArrayString) {

        Type listType = new TypeToken<CompanyProfileModel>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static ArrayList<CompanyHeadModel> GsonToCompanyProfileList(Context context, String recordsArrayString) {

        Type listType = new TypeToken<List<CompanyHeadModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }




//    public static Class getGSONConvertedClass(Context context, String recordsArrayString, Class<T> convertedClass){
//
//        return new Gson().fromJson(recordsArrayString, convertedClass.getClass());
//    }


}
