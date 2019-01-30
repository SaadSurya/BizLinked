package com.application.lumaque.bizlinked.helpers.network;


import com.application.lumaque.bizlinked.data_models.bizlinked.BusinessNatureModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CitiesModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyHeadModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.MajorCategoryModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.Product;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonHelper {


    public static ProductList GsonToProductList(String recordsArrayString) {

        Type listType = new TypeToken<ProductList>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }


    public static Product GsonToProduct(String recordsArrayString) {

        Type listType = new TypeToken<Product>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }


    public static ArrayList<ProductCategory> GsonToCategoryList(String recordsArrayString) {

        Type listType = new TypeToken<List<ProductCategory>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static ProductCategory GsonToProductCategory(String recordsArrayString) {

        Type listType = new TypeToken<ProductCategory>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }


    public static ArrayList<CitiesModel> GsonToCities(String recordsArrayString) {

        Type listType = new TypeToken<List<CitiesModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static ArrayList<BusinessNatureModel> GsonToBusinessNature(String recordsArrayString) {

        Type listType = new TypeToken<List<BusinessNatureModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static ArrayList<MajorCategoryModel> GsonToMajorCategory(String recordsArrayString) {

        Type listType = new TypeToken<List<MajorCategoryModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static CompanyProfileModel GsonToCompanyProfile(String recordsArrayString) {

        Type listType = new TypeToken<CompanyProfileModel>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }

    public static ArrayList<CompanyHeadModel> GsonToCompanyProfileList(String recordsArrayString) {

        Type listType = new TypeToken<List<CompanyHeadModel>>() {
        }.getType();
        return new Gson().fromJson(recordsArrayString, listType);
    }


    public String getJsonString(Object object) {


        Gson gson = new Gson();
        return gson.toJson(object);

    }

//    public static Class getGSONConvertedClass(Context context, String recordsArrayString, Class<T> convertedClass){
//
//        return new Gson().fromJson(recordsArrayString, convertedClass.getClass());
//    }


}
