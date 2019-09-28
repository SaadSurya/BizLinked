package com.application.lumaque.bizlinked.helpers.network;


import java.util.HashMap;
import java.util.Map;

public class CommonNetworksUtils {

    public static String getRequestURI(HashMap<String, String> params) {
        StringBuilder paramsStr = new StringBuilder("");
        if (params != null) {
            paramsStr.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                paramsStr.append(key).append("=").append(value).append("&");
            }
            paramsStr.deleteCharAt(paramsStr.lastIndexOf("&"));

        }
        return paramsStr.toString();
    }

    public static String getDeleteURI(HashMap<String, String> params) {
        StringBuilder paramsStr = new StringBuilder("");
        if (params != null) {
            paramsStr.append("/");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                paramsStr.append(value).append("/");
            }
            paramsStr.deleteCharAt(paramsStr.lastIndexOf("/"));

        }
        return paramsStr.toString();
    }
}
