package com.application.lumaque.bizlinked.helpers.network;

import com.android.volley.NetworkResponse;

import java.io.InputStream;
import java.util.Map;


public class InputStreamNetworkResponse extends NetworkResponse {
    public final InputStream ins;

    /**
     * Creates a new network response.
     *
     * @param statusCode  the HTTP status code
     * @param data        Response body
     * @param headers     Headers returned with this response, or null for none
     * @param notModified True if the server returned a 304 and the data was already in cache
     */
    public InputStreamNetworkResponse(int statusCode, byte[] data, InputStream ins, Map<String, String> headers,
                                      boolean notModified) {
        super(statusCode, data, headers, notModified);
        this.ins = ins;
    }

    public InputStreamNetworkResponse(byte[] data, InputStream ins) {
        super(data);
        this.ins = ins;
    }

    public InputStreamNetworkResponse(byte[] data, InputStream ins, Map<String, String> headers) {
        super(data, headers);
        this.ins = ins;
    }
}

