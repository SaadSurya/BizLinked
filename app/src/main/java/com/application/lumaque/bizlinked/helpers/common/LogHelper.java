package com.application.lumaque.bizlinked.helpers.common;

import android.util.Log;

public class LogHelper {
    private static LogHelper helper = new LogHelper();

    private final String TAG = "tag_";

    public static LogHelper getLogger() {
        return helper;
    }

    public void log(String message) {
        Log.i(TAG, message);
    }


}
