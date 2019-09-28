package com.application.lumaque.bizlinked.helpers.network;


import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.application.lumaque.bizlinked.R;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyHelper {


    public static String getErrorMsg(Context context, VolleyError error) {
        String ret = "";
        NetworkResponse response = error.networkResponse;
        if (error.toString().equals("com.android.volley.TimeoutError")) {
            ret = context.getString(R.string.ws_error_timeout);
        } else if (response != null && response.data != null) {
            String respData = new String(response.data);
            JSONObject jsonObj = null;
            System.out.println(respData);
            switch (response.statusCode) {
                case 400:
                    try {
                        jsonObj = new JSONObject(respData);
                        ret = jsonObj.getString("description");
                    } catch (JSONException e) {
                        try {
                            ret = jsonObj.getString("message");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            //ret = SSCApplication.getContext().getResources().getString(R.string.ws_error_general);
                            ret = respData;
                        }
                    }
                    break;
                case 401:
                    try {
                        jsonObj = new JSONObject(respData);
                        ret = jsonObj.getString("description");
                    } catch (JSONException e) {
                        try {
                            //ret = jsonObj.getString("message");
                            ret = context.getResources().getString(R.string.token_expired);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            ret = context.getResources().getString(R.string.ws_error_general);
                        }
                    }
                        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(SSCApplication.getContext())
                        .setCancelable(false)
                        .setTitle("Session expired")
                        .setMessage("Your session has expired. Please login again to continue")
                        .setPositiveButton("Proceed to login screen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        SSCApplication.getContext().startActivity(new Intent("technologies.centegy.illustration.LoginScreen"));
                        }
                        });
                        alertDialog.show();*/
                    break;


                case 500:
                    try {
                        jsonObj = new JSONObject(respData);
                        ret = jsonObj.getString("ExceptionMessage");
                    } catch (JSONException e) {
                        try {
                            ret = context.getResources().getString(R.string.server_error);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            ret = context.getResources().getString(R.string.ws_error_general);
                        }

                    }
                    break;
                default:
                    try {
                        jsonObj = new JSONObject(respData);
                        ret = jsonObj.getString("description");
                    } catch (JSONException e) {
                        try {
                            ret = jsonObj.getString("message");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            //ret = SSCApplication.getContext().getResources().getString(R.string.ws_error_general);
                            if (ret.equals("Server Error")) {
                                ret = context.getResources().getString(R.string.server_error);
                            } else {

                                ret = respData;
                            }
                        }
                    }
                    break;
            }


        } else {
            ret = error.getMessage();
        }

        return ret;
    }

}
