package com.application.lumaque.bizlinked.webhelpers;

import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.BusinessNatureModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CompanyHelper {

    private BaseActivity activityReference;
    private BasePreferenceHelper preferenceHelper;
    private ArrayList<ProductCategory> categoryLIst;

    public CompanyHelper(BaseActivity activityReference,BasePreferenceHelper preferenceHelper) {

        this.activityReference = activityReference;
        this.preferenceHelper = preferenceHelper;

    }

    public   void getCompanyCategoty(String CompanyId){



        HashMap<String, String> params = new HashMap<>();
        params.put("companyId",CompanyId);



        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_CATEGORY,false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

                preferenceHelper.putCategory(response);


            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });



    }

    public   void getCompanyAttributes(String CompanyId){



        HashMap<String, String> params = new HashMap<>();
        params.put("companyId",CompanyId);



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
