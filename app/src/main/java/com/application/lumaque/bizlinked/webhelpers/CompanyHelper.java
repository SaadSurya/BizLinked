package com.application.lumaque.bizlinked.webhelpers;

import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.fragments.bizlinked.ResponceCallBack;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CompanyHelper {

    private BaseActivity activityReference;
    private BasePreferenceHelper preferenceHelper;
    private ArrayList<ProductCategory> categoryLIst;


    private ResponceCallBack responceCallBack;
    public CompanyHelper(BaseActivity activityReference,BasePreferenceHelper preferenceHelper,ResponceCallBack responceCallBack) {

        this.activityReference = activityReference;
        this.preferenceHelper = preferenceHelper;
        this.responceCallBack = responceCallBack;

    }

    public   void getCompanyCategoty(int CompanyId){



        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(CompanyId));



        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_CATEGORY,false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

            //    preferenceHelper.putCategory(response);

                GsonHelper gsonHelper = new GsonHelper();


                responceCallBack.onCategoryResponce(gsonHelper.GsonToCategoryList(activityReference,response));
            }

            @Override
            public void onError(String response) {

                responceCallBack.onCategoryResponce(null);
            }

            @Override
            public void onNoNetwork() {

            }
        });



    }

    public   void getCompanyAttributes(int CompanyId){



        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(CompanyId));



        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_ATTRIBUTES,false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

                preferenceHelper.putAttributes(response);


            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });



    }


}
