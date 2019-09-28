package com.application.lumaque.bizlinked.webServices;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<NetworkResponse> {

    private String twoHyphens = "--";
    private String lineEnd = "\r\n";
    private String boundary = "apiclient-" + System.currentTimeMillis();
    private byte[] multipartBody = null;

    private Map<String, String> headerParams;
    private Map<String, String> postParams;
    private Response.ErrorListener errorListener;
    private Response.Listener<String> listener;


    public MultipartRequest(int method, String url, Response.Listener<String> networkResponse
            , Response.ErrorListener listener, Map<String, String> headerParams, Map<String, String> postParams) {
        super(method, url, listener);

        this.headerParams = headerParams;
        this.postParams = postParams;
        this.errorListener = listener;
        this.listener = networkResponse;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(new String(response.data));
    }

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


    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return multipartBody;
    }

    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                + fileName + "\"" + lineEnd);

        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    public void setMultipartBody(String fileName, byte[] fileData) {


        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream2);

        Map<String, String> params = getParams();
        try {
            if (params != null && params.size() > 0) {
                textParse(dataOutputStream, params, getParamsEncoding());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            buildPart(dataOutputStream, fileData, fileName);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            multipartBody = byteArrayOutputStream2.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }
}
