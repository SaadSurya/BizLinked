package com.application.lumaque.bizlinked.helpers.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null)
            return false;

        // 3g-4g available
        boolean is3g = false;
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
            is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .isConnectedOrConnecting();
        }
        // wifi available
        boolean isWifi = false;
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
            isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting();

        }

        System.out.println(is3g + " net " + isWifi);


        return (is3g || isWifi);
    }
}
