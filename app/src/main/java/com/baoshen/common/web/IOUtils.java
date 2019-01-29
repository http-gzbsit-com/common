package com.baoshen.common.web;

import android.text.TextUtils;

import com.baoshen.common.Log;
import com.baoshen.common.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class IOUtils {
    private static final String TAG = IOUtils.class.getSimpleName();

    public static InputStream post(String url, byte[] data,String contentType) throws Exception{
        URL uRL = new URL(url);
        URLConnection rulConnection = uRL.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setRequestMethod("POST");
        if (!TextUtils.isEmpty(contentType))
        {
            httpUrlConnection.addRequestProperty("Content-Type",contentType);
        }
        httpUrlConnection.connect();
        OutputStream outStrm = httpUrlConnection.getOutputStream();

        outStrm.write(data);
        outStrm.flush();
        outStrm.close();
        InputStream inStrm = httpUrlConnection.getInputStream();
        return inStrm;
    }

    public static InputStream get(String url, Map<String,String> params) throws Exception{
        if (params!=null)
        {
            String encodeParams =Utils.TransMapToString(params);
            url=url+"?"+encodeParams;
        }
        Log.i(TAG,"对url中的参数进行url编码:"+url);
        URL uRL = new URL(url);
        URLConnection rulConnection = uRL.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.connect();
        InputStream inStrm = httpUrlConnection.getInputStream();
        return inStrm;
    }
}
