package com.prj666.recycling_vision;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class MultipartRequest extends Request<NetworkResponse> {

    private Map<String, String> headers;
    private Response.Listener<NetworkResponse> listener;
    private Response.ErrorListener error;
    private String filename;
    private byte[] img;

    public MultipartRequest(int method, String url, Map<String, String> newHeaders, String newFilename, byte[] newImg, Response.Listener<NetworkResponse> newListener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.headers = newHeaders;
        this.listener = newListener;
        this.error = errorListener;
        this.filename = newFilename;
        this.img = newImg;
    }

    public MultipartRequest(String url, Map<String, String> newHeaders, String newFilename, byte[] newImg, Response.Listener<NetworkResponse> newListener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.headers = newHeaders;
        this.listener = newListener;
        this.error = errorListener;
        this.filename = newFilename;
        this.img = newImg;
    }

    @Override
    public String getBodyContentType(){
        return "multipart/form-data;boundary=" + Long.toHexString(System.currentTimeMillis());
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(this.headers != null){
            return this.headers;
        }
        else{
            return super.getHeaders();
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(baos);

        try {
            data.writeBytes("--" + Long.toHexString(System.currentTimeMillis()) +
                    "\nContent-disposition: form-data; name=\"file\"; filename=\"" + filename + //may need to manually get file type from user
                    "\"\nContent-type: multipart/formdata\nConnection: keep-alive\n\n");
            data.write(img);
            data.writeBytes("\n--" + Long.toHexString(System.currentTimeMillis()) + "\n"); //all new lines may need \r as well?
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response)); //may need to catch exceptions
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }
}
