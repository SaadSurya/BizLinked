package com.application.lumaque.bizlinked.webServices;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APISingletonHelperClass {

    private static final APISingletonHelperClass ourInstance = new APISingletonHelperClass();
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private APISingletonHelperClass() {
    }

    public static APISingletonHelperClass getInstance(Context context) {
        mContext = context;
        return ourInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
