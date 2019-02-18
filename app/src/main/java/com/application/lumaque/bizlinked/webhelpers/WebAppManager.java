package com.application.lumaque.bizlinked.webhelpers;

import android.app.Activity;

import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.helpers.common.LogHelper;
import com.application.lumaque.bizlinked.helpers.network.CommonNetworksUtils;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import static com.application.lumaque.bizlinked.helpers.common.Utils.getBytes;

public class WebAppManager {

    private static Activity activity;
    private static BasePreferenceHelper preferenceHelper;

    private static final WebAppManager ourInstance = new WebAppManager();

    public static WebAppManager getInstance(Activity ctx, BasePreferenceHelper preference) {
        activity = ctx;
        preferenceHelper = preference;
        return ourInstance;
    }

    private WebAppManager() {
    }




    public void getMediaFile(boolean isProgressShow, String url, int type, HashMap<String, String> extraParams, final APIStringRequestDataCallBack apiStringRequestDataCallBack) {




        if (type == AppConstant.ServerAPICalls.HTTP_VERBS.GET)
            url = url + CommonNetworksUtils.getRequestURI(extraParams);


        WebAPIRequestHelper.getInstance(activity, isProgressShow)
                .setHeaderUserPreference(preferenceHelper)
               // .setCustomBody(type == AppConstant.ServerAPICalls.HTTP_VERBS.POST ? extraParams : null)
                .mediaRequest(
                        extraParams,
                        type,
                        url,
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                apiStringRequestDataCallBack.onSuccess(response);
                            }

                            @Override
                            public void onError(String response) {
                                apiStringRequestDataCallBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                apiStringRequestDataCallBack.onNoNetwork();
                            }
                        }
                );
    }

    public void getAllGridDetails(HashMap<String, String> extraParams, String url,boolean isProgressShow, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (extraParams == null)
            LogHelper.getLogger().log("Request" + url);
        else
            LogHelper.getLogger().log("Request" + url + extraParams.toString());




        WebAPIRequestHelper.getInstance(activity, isProgressShow)
                .setHeaderUserPreference(preferenceHelper)
                .getStringRequest(
                        url +
                                CommonNetworksUtils.getRequestURI(extraParams),
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {


                                    apiRequestDataCallBack.onSuccess(response);

                            }

                            @Override
                            public void onError(String response) {
                                LogHelper.getLogger().log("Response Error " + response);

                                apiRequestDataCallBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                apiRequestDataCallBack.onNoNetwork();
                            }
                        }
                );
    }



 public void deleteDetails(HashMap<String, String> extraParams, String url,boolean isProgressShow, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (extraParams == null)
            LogHelper.getLogger().log("Request" + url);
        else
            LogHelper.getLogger().log("Request" + url + extraParams.toString());




        WebAPIRequestHelper.getInstance(activity, isProgressShow)
                .setHeaderUserPreference(preferenceHelper)
                .deleteRequest(
                        url +
                                CommonNetworksUtils.getDeleteURI(extraParams),
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {


                                    apiRequestDataCallBack.onSuccess(response);

                            }

                            @Override
                            public void onError(String response) {
                                LogHelper.getLogger().log("Response Error " + response);

                                apiRequestDataCallBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                apiRequestDataCallBack.onNoNetwork();
                            }
                        }
                );
    }

    public void saveDetails(int requestType, HashMap<String, String> extraParams, String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (extraParams == null)
            LogHelper.getLogger().log("Post Request" + url);
        else
            LogHelper.getLogger().log("Post Request" + url + extraParams.toString());


       /* HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put("currentCompanyId", (preferenceHelper.getCompanyProfile())== null ? "" :String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()) );*/
        HashMap<String, String> params = new HashMap<>();
       /* params.put("appCode", preferenceHelper.getUser().getAppCode());
        params.put("clientid", preferenceHelper.getUser().getClientid());
        params.put("currpage", "1");
        params.put("orgacode", preferenceHelper.getUser().getOrgacode());
        params.put("pagesize", "1000");
        if (!Utils.isEmptyOrNull(preferenceHelper.getUser().getQei()))
            params.put("qei", preferenceHelper.getUser().getQei());
        else
            params.put("qei", "");

        params.put("userType", preferenceHelper.getUser().getClienttype());
        params.put("totalpages", "0");*/

        if (extraParams != null)
            params.putAll(extraParams);


        WebAPIRequestHelper.getInstance(activity, true)
               .setHeaderUserPreference(preferenceHelper)
                .setCustomBody(params)
                .postRequest(
                        requestType,
                        url,
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {


                                    LogHelper.getLogger().log("Post Response " + response);
                                    apiRequestDataCallBack.onSuccess(response);




                            }

                            @Override
                            public void onError(String response) {
                                LogHelper.getLogger().log("Post Response Error " + response);

                                apiRequestDataCallBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                apiRequestDataCallBack.onNoNetwork();
                            }
                        }
                );
    }




   public void putDetails(int requestType, HashMap<String, String> extraParams, String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (extraParams == null)
            LogHelper.getLogger().log("Post Request" + url);
        else
            LogHelper.getLogger().log("Post Request" + url + extraParams.toString());


       /* HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put("currentCompanyId", (preferenceHelper.getCompanyProfile())== null ? "" :String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()) );*/
        HashMap<String, String> params = new HashMap<>();
       /* params.put("appCode", preferenceHelper.getUser().getAppCode());
        params.put("clientid", preferenceHelper.getUser().getClientid());
        params.put("currpage", "1");
        params.put("orgacode", preferenceHelper.getUser().getOrgacode());
        params.put("pagesize", "1000");
        if (!Utils.isEmptyOrNull(preferenceHelper.getUser().getQei()))
            params.put("qei", preferenceHelper.getUser().getQei());
        else
            params.put("qei", "");

        params.put("userType", preferenceHelper.getUser().getClienttype());
        params.put("totalpages", "0");*/

        if (extraParams != null)
            params.putAll(extraParams);


        WebAPIRequestHelper.getInstance(activity, true)
               .setPutHeaderUserPreference(preferenceHelper)
                .setCustomBody(params)
                .postRequest(
                        requestType,
                        url,
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {


                                    LogHelper.getLogger().log("Post Response " + response);
                                    apiRequestDataCallBack.onSuccess(response);




                            }

                            @Override
                            public void onError(String response) {
                                LogHelper.getLogger().log("Post Response Error " + response);

                                apiRequestDataCallBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                apiRequestDataCallBack.onNoNetwork();
                            }
                        }
                );
    }



    public void saveDetailsJson(int requestType, String jsonString, String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {



        WebAPIRequestHelper.getInstance(activity, true)
                .setHeaderUserPreference(preferenceHelper)
                .setCustomJsonBody(jsonString)
                .postJsonRequest(
                        requestType,
                        url,
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {


                                    LogHelper.getLogger().log("Post Response " + response);
                                    apiRequestDataCallBack.onSuccess(response);




                            }

                            @Override
                            public void onError(String response) {
                                LogHelper.getLogger().log("Post Response Error " + response);

                                apiRequestDataCallBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                apiRequestDataCallBack.onNoNetwork();
                            }
                        }
                );
    }

    public void uploadImage(String fileName, HashMap<String, String> extraParams,String URL, Boolean showProgress,File file, final WebAPIRequestHelper.APIStringRequestDataCallBack apiRequestDataCallBack) {



        WebAPIRequestHelper.getInstance(activity, showProgress)
               // .setHeaderUserPreference(preferenceHelper)
                //.setCustomBody(extraParams)
                .postRequestMultipart(getBytes(file),
                        fileName, URL

                        , apiRequestDataCallBack
                );
    }


  /*  public void getAllListUIFields(HashMap<String, String> extraParams, String url, final APIRequestFieldsUIDataCallBack apiRequestDataCallBack) {

        if (extraParams == null)
            LogHelper.getLogger().log("Request" + url);
        else
            LogHelper.getLogger().log("Request" + url + extraParams.toString());

        HashMap<String, String> params = new HashMap<>();
        params.put("appCode", preferenceHelper.getUser().getAppCode());
        params.put("currpage", "1");
        params.put("pagesize", "1000");
        params.put("totalpages", "0");

        if (extraParams != null)
            params.putAll(extraParams);


        LogHelper.getLogger().log("Final parameters " + params.toString());

        WebAPIRequestHelper.getInstance(activity, true)
                //.setHeaderUserPreference(preferenceHelper)
                .getStringRequest(
                        AppConstant.ServerAPICalls.IPORTAL_URL +
                                preferenceHelper.getUser().getAppCode() + "/" +
                                url +
                                CommonNetworksUtils.getRequestURI(params),
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {

                                try {

                                    LogHelper.getLogger().log("Response" + response);

                                    JSONObject responseObj = new JSONObject(response);
                                    JSONArray recordsArray = responseObj.getJSONArray("records");
                                    String recordsArrayString = recordsArray.toString();
                                    apiRequestDataCallBack.onSuccess(GsonHelper.getGSONConverted2(activity, recordsArrayString));
                                } catch (JSONException e) {
                                    apiRequestDataCallBack.onError(response);
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String response) {
                                LogHelper.getLogger().log("Response Error " + response);

                                apiRequestDataCallBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                apiRequestDataCallBack.onNoNetwork();
                            }
                        }
                );
    }
*/
 /*   public void getDropDownData
            (HashMap<String, String> extraParams, String url, final APIRequestDropdownCallBack callBack) {

        if (extraParams == null)
            LogHelper.getLogger().log("Request" + url);
        else
            LogHelper.getLogger().log("Request" + url + extraParams.toString());

        HashMap<String, String> params = new HashMap<>();
        params.put("appCode", preferenceHelper.getUser().getAppCode());
        params.put("currpage", "1");
        params.put("pagesize", "1000");
        params.put("totalpages", "0");

        if (extraParams != null)
            params.putAll(extraParams);


        LogHelper.getLogger().log("Final parameters " + params.toString());

        WebAPIRequestHelper.getInstance(activity, true)
                .getStringRequest(
                        AppConstant.ServerAPICalls.IPORTAL_URL +
                                preferenceHelper.getUser().getAppCode() + "/" +
                                url +
                                CommonNetworksUtils.getRequestURI(params),
                        new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                LogHelper.getLogger().log("Response" + response);
                                callBack.onSuccess(GsonHelper.parseDataForDropdown(response));
                            }

                            @Override
                            public void onError(String response) {
                                LogHelper.getLogger().log("Response Error " + response);
                                callBack.onError(response);
                            }

                            @Override
                            public void onNoNetwork() {
                                callBack.onNoNetwork();
                            }
                        }
                );
    }

*/
    public interface APIStringRequestDataCallBack {
        void onSuccess(String response);

        void onError(String response);

        void onNoNetwork();
    }






}
