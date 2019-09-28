package com.application.lumaque.bizlinked.webhelpers;


import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.helpers.common.KeyboardHelper;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.NetworkUtils;
import com.application.lumaque.bizlinked.helpers.network.VolleyHelper;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.webServices.APISingletonHelperClass;
import com.application.lumaque.bizlinked.webServices.MultipartRequest;
import com.application.lumaque.bizlinked.webServices.VolleyMediaRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WebAPIRequestHelper {

    public static final String TAG = "WebAPIRequestHelper";
    private static final int SECONDS = 10;
    private static APISingletonHelperClass apiService;
    private static Activity currentActivity;
    private static WebAPIRequestHelper ourInstance = new WebAPIRequestHelper();
    private Map<String, String> headerParams;
    private Map<String, String> postParams;
    private String jsonString;


    private WebAPIRequestHelper() {
    }

    /**
     * Multipart request variables
     **/


    public static WebAPIRequestHelper getInstance(Activity activity, boolean isProgressShow) {
        apiService = APISingletonHelperClass.getInstance(activity);
        currentActivity = activity;
        if (isProgressShow) {
            ((BaseActivity) currentActivity).onLoadingStarted();
            KeyboardHelper.hideSoftKeyboard(currentActivity);
        }

        return ourInstance;
    }

    public WebAPIRequestHelper setHeaderUserPreference(BasePreferenceHelper preference) {
        headerParams = new HashMap<>();

        headerParams.put("currentCompanyId", (preference.getCompanyProfile()) == null ? "" : String.valueOf(preference.getCompanyProfile().getCompanyID()));
        ;

        return this;

    }

    public WebAPIRequestHelper setPutHeaderUserPreference(BasePreferenceHelper preference) {
        headerParams = new HashMap<>();

        headerParams.put("currentCompanyId", (preference.getCompanyProfile()) == null ? "" : String.valueOf(preference.getCompanyProfile().getCompanyID()));
        ;

        return this;

    }

    public WebAPIRequestHelper setCustomHeader(Map<String, String> params) {
        headerParams = new HashMap<String, String>();
        if (params != null)
            headerParams.putAll(params);
        return this;
    }

    public WebAPIRequestHelper setCustomBody(Map<String, String> params) {
        postParams = new HashMap<String, String>();
        if (params != null)
            postParams.putAll(params);
        return this;
    }

    public WebAPIRequestHelper setCustomJsonBody(String jsonArrayString) {
        jsonString = jsonArrayString;

        return this;
    }

    public void getStringRequest(final String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (!NetworkUtils.isNetworkAvailable(currentActivity)) {

            ((BaseActivity) currentActivity).onLoadingFinished();
            apiRequestDataCallBack.onNoNetwork();
            Utils.showSnackBar(currentActivity, currentActivity.findViewById(R.id.fragmentContainer),
                    currentActivity.getResources().getString(R.string.no_network_available),
                    ContextCompat.getColor(currentActivity, R.color.grayColor));
            return;
        }

        final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlEncoded, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ((BaseActivity) currentActivity).onLoadingFinished();

                if (!Utils.isEmptyOrNull(response)) {
                    apiRequestDataCallBack.onSuccess(response);
                } else {
                    apiRequestDataCallBack.onError(null);
                    Utils.showToast(currentActivity, currentActivity.getString(R.string.something_went_wrong), AppConstant.TOAST_TYPES.ERROR);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                ((BaseActivity) currentActivity).onLoadingFinished();
                apiRequestDataCallBack.onError(VolleyHelper.getErrorMsg(currentActivity, error));
                Utils.showToast(currentActivity, VolleyHelper.getErrorMsg(currentActivity, error), AppConstant.TOAST_TYPES.ERROR);


            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                if (headerParams != null)
                    params.putAll(headerParams);
                return params;
            }
        };

        strReq.setRetryPolicy(
                new DefaultRetryPolicy(SECONDS * 1000,// N sec
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        // Adding String request to request queue
        APISingletonHelperClass.getInstance(currentActivity).addToRequestQueue(strReq, url);
    }

    public void deleteRequest(final String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (!NetworkUtils.isNetworkAvailable(currentActivity)) {

            ((BaseActivity) currentActivity).onLoadingFinished();
            apiRequestDataCallBack.onNoNetwork();
            Utils.showSnackBar(currentActivity, currentActivity.findViewById(R.id.fragmentContainer),
                    currentActivity.getResources().getString(R.string.no_network_available),
                    ContextCompat.getColor(currentActivity, R.color.grayColor));
            return;
        }

        final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);

        StringRequest strReq = new StringRequest(Request.Method.DELETE, urlEncoded, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ((BaseActivity) currentActivity).onLoadingFinished();

                if (!Utils.isEmptyOrNull(response)) {
                    apiRequestDataCallBack.onSuccess(response);
                } else {
                    apiRequestDataCallBack.onError(null);
                    Utils.showToast(currentActivity, currentActivity.getString(R.string.something_went_wrong), AppConstant.TOAST_TYPES.ERROR);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                ((BaseActivity) currentActivity).onLoadingFinished();
                apiRequestDataCallBack.onError(VolleyHelper.getErrorMsg(currentActivity, error));
                Utils.showToast(currentActivity, VolleyHelper.getErrorMsg(currentActivity, error), AppConstant.TOAST_TYPES.ERROR);


            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                if (headerParams != null)
                    params.putAll(headerParams);
                return params;
            }
        };

        strReq.setRetryPolicy(
                new DefaultRetryPolicy(SECONDS * 1000,// N sec
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        // Adding String request to request queue
        APISingletonHelperClass.getInstance(currentActivity).addToRequestQueue(strReq, url);
    }

    public void postRequest(int type, final String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (!NetworkUtils.isNetworkAvailable(currentActivity)) {

            ((BaseActivity) currentActivity).onLoadingFinished();
            apiRequestDataCallBack.onNoNetwork();
            Utils.showSnackBar(currentActivity, currentActivity.findViewById(R.id.fragmentContainer),
                    currentActivity.getResources().getString(R.string.no_network_available),
                    ContextCompat.getColor(currentActivity, R.color.grayColor));
            return;
        }

        StringRequest strReq = new StringRequest(type, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ((BaseActivity) currentActivity).onLoadingFinished();

                if (!Utils.isEmptyOrNull(response)) {
                    apiRequestDataCallBack.onSuccess(response);
                } else {
                    apiRequestDataCallBack.onError(null);
                    Utils.showToast(currentActivity, currentActivity.getString(R.string.something_went_wrong), AppConstant.TOAST_TYPES.ERROR);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                ((BaseActivity) currentActivity).onLoadingFinished();
                apiRequestDataCallBack.onError(VolleyHelper.getErrorMsg(currentActivity, error));
                Utils.showToast(currentActivity, VolleyHelper.getErrorMsg(currentActivity, error), AppConstant.TOAST_TYPES.ERROR);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (postParams != null)
                    params.putAll(postParams);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                if (headerParams != null)
                    params.putAll(headerParams);
                return params;
            }
        };

        strReq.setRetryPolicy(
                new DefaultRetryPolicy(SECONDS * 1000,// N sec
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        // Adding String request to request queue
        APISingletonHelperClass.getInstance(currentActivity).addToRequestQueue(strReq, url);
    }

    public void postJsonRequest(int type, final String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (!NetworkUtils.isNetworkAvailable(currentActivity)) {

            ((BaseActivity) currentActivity).onLoadingFinished();
            apiRequestDataCallBack.onNoNetwork();
            Utils.showSnackBar(currentActivity, currentActivity.findViewById(R.id.fragmentContainer),
                    currentActivity.getResources().getString(R.string.no_network_available),
                    ContextCompat.getColor(currentActivity, R.color.grayColor));
            return;
        }

        StringRequest strReq = new StringRequest(type, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ((BaseActivity) currentActivity).onLoadingFinished();

                if (!Utils.isEmptyOrNull(response)) {
                    apiRequestDataCallBack.onSuccess(response);
                } else {
                    apiRequestDataCallBack.onError(null);
                    Utils.showToast(currentActivity, currentActivity.getString(R.string.something_went_wrong), AppConstant.TOAST_TYPES.ERROR);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                ((BaseActivity) currentActivity).onLoadingFinished();
                apiRequestDataCallBack.onError(VolleyHelper.getErrorMsg(currentActivity, error));
                Utils.showToast(currentActivity, VolleyHelper.getErrorMsg(currentActivity, error), AppConstant.TOAST_TYPES.ERROR);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonString.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                if (headerParams != null)
                    params.putAll(headerParams);
                return params;
            }
        };

        strReq.setRetryPolicy(
                new DefaultRetryPolicy(SECONDS * 1000,// N sec
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        // Adding String request to request queue
        APISingletonHelperClass.getInstance(currentActivity).addToRequestQueue(strReq, url);
    }

    public void mediaRequest(HashMap<String, String> extraParameters, int type, String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (!NetworkUtils.isNetworkAvailable(currentActivity)) {

            ((BaseActivity) currentActivity).onLoadingFinished();
            apiRequestDataCallBack.onNoNetwork();
            Utils.showSnackBar(currentActivity, currentActivity.findViewById(R.id.fragmentContainer),
                    currentActivity.getResources().getString(R.string.no_network_available),
                    ContextCompat.getColor(currentActivity, R.color.grayColor));
            return;
        }

        final String _loc = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
        final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);
        int requestType = 0;

        if (type == AppConstant.ServerAPICalls.HTTP_VERBS.POST)
            requestType = Request.Method.POST;
        else if (type == AppConstant.ServerAPICalls.HTTP_VERBS.GET)
            requestType = Request.Method.GET;


        final String fileName = System.currentTimeMillis() + "";

        VolleyMediaRequest request = new VolleyMediaRequest(requestType, urlEncoded, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    // read this file into InputStream
                    inputStream = new ByteArrayInputStream(response);
                    //  inputStream = new BufferedInputStream(input,1024);

                    // write the inputStream to a FileOutputStream
                    String filePath = new StringBuilder(_loc)
                            .append("/")
                            .append(fileName)
                            .toString();
                    outputStream = new FileOutputStream(new File(filePath));

                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }


                    String fPath = _loc + fileName;

                    ((BaseActivity) currentActivity).onLoadingFinished();

                    if (!Utils.isEmptyOrNull(fPath)) {
                        apiRequestDataCallBack.onSuccess(fPath);
                    } else {
                        apiRequestDataCallBack.onError(null);
                        Utils.showToast(currentActivity, currentActivity.getString(R.string.something_went_wrong), AppConstant.TOAST_TYPES.ERROR);
                    }
                } catch (Exception e) {
                    apiRequestDataCallBack.onError(null);
                    //Utils.showToast(currentActivity, e + "", AppConstant.TOAST_TYPES.ERROR);
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.flush();
                            outputStream.getFD().sync();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    ((BaseActivity) currentActivity).onLoadingFinished();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).onLoadingFinished();
                apiRequestDataCallBack.onError(VolleyHelper.getErrorMsg(currentActivity, error));
                //Utils.showToast(currentActivity, VolleyHelper.getErrorMsg(currentActivity, error), AppConstant.TOAST_TYPES.ERROR);

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (headerParams != null)
                    params.putAll(headerParams);
                return params;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (postParams != null)
                    params.putAll(postParams);
                return params;
            }
        };


        // Adding String request to request queue
        request.setRetryPolicy(
                new DefaultRetryPolicy(5 * 60 * 1000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        APISingletonHelperClass.getInstance(currentActivity).addToRequestQueue(request, url);
    }

    public void postRequestMultipart(byte[] fileData, String fileName, final String url, final APIStringRequestDataCallBack apiRequestDataCallBack) {

        if (!NetworkUtils.isNetworkAvailable(currentActivity)) {

            ((BaseActivity) currentActivity).onLoadingFinished();
            apiRequestDataCallBack.onNoNetwork();
            Utils.showSnackBar(currentActivity, currentActivity.findViewById(R.id.fragmentContainer),
                    currentActivity.getResources().getString(R.string.no_network_available),
                    ContextCompat.getColor(currentActivity, R.color.grayColor));
            return;
        }


        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((BaseActivity) currentActivity).onLoadingFinished();

                if (!Utils.isEmptyOrNull(response)) {
                    apiRequestDataCallBack.onSuccess(response);
                } else {
                    apiRequestDataCallBack.onError(null);
                    Utils.showToast(currentActivity, currentActivity.getString(R.string.something_went_wrong), AppConstant.TOAST_TYPES.ERROR);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                ((BaseActivity) currentActivity).onLoadingFinished();
                apiRequestDataCallBack.onError(VolleyHelper.getErrorMsg(currentActivity, error));
                Utils.showToast(currentActivity, VolleyHelper.getErrorMsg(currentActivity, error), AppConstant.TOAST_TYPES.ERROR);
            }
        };

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST, url
                , listener, errorListener, headerParams, postParams
        );

        multipartRequest.setMultipartBody(fileName, fileData);

        multipartRequest.setRetryPolicy(
                new DefaultRetryPolicy(SECONDS * 1000,// N sec
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        // Adding String request to request queue
        APISingletonHelperClass.getInstance(currentActivity).addToRequestQueue(multipartRequest, url);

    }


    public interface APIStringRequestDataCallBack {
        void onSuccess(String response);

        void onError(String response);

        void onNoNetwork();
    }

    public interface APIJsonObjectRequestDataCallBack {
        void onSuccess(JSONObject response);

        void onError(String response);

        void onNoNetwork();
    }


    public interface APIJsonArrayRequestDataCallBack {
        void onSuccess(JSONArray response);

        void onError(String response);

        void onNoNetwork();
    }


}






