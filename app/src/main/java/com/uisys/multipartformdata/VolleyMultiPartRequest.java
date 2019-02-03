package com.uisys.multipartformdata;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by Admin on 12/22/18.
 */

public class VolleyMultiPartRequest extends Request {
    public VolleyMultiPartRequest(int method, String url, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);
    }


    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        Log.d("@Ramesh","on Response" );
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d("@Ramesh",json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(Object response) {

    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
