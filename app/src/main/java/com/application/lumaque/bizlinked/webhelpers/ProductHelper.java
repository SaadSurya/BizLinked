package com.application.lumaque.bizlinked.webhelpers;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;

public class ProductHelper {
    private BasePreferenceHelper preferenceHelper;
    private BaseActivity activityReference;
    public ProductHelper( BasePreferenceHelper preferenceHelper,BaseActivity activityReference) {
        this.preferenceHelper =preferenceHelper;
        this.activityReference =activityReference;


    }

    public void putService(String jsonString, String URL, final WebAppManager.APIStringRequestDataCallBack apiStringRequestDataCallBack) {
        //AppConstant.ServerAPICalls.PRODUCT_SAVE
        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetailsJson(
                Request.Method.PUT,
                jsonString,URL, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {


                        apiStringRequestDataCallBack.onSuccess(response);
                    /*    CompanyProfileModel companyprofile = GsonHelper.GsonToCompanyProfile(activityReference, response);

                        preferenceHelper.putCompany(companyprofile);
                        Utils.showToast(activityReference, "Profile Updater", AppConstant.TOAST_TYPES.SUCCESS);
                        activityReference.updateDrawer();*/

                    }

                    @Override
                    public void onError(String response) {
                        //     Utils.showToast(activityReference, "error", AppConstant.TOAST_TYPES.SUCCESS);
                        apiStringRequestDataCallBack.onError(response);
                    }

                    @Override
                    public void onNoNetwork() {
                        apiStringRequestDataCallBack.onNoNetwork();
                    }
                });


    }





}
