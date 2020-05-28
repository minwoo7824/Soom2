package com.kmw.soom2.Common;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnection {

    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();
    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection(){ this.client = new OkHttpClient(); }


    /** 웹 서버로 요청을 한다. */
    public void requestWebServer(String parameter, String parameter2, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("parameter", parameter)
                .add("parameter2", parameter2)
                .build();
        Request request = new Request.Builder()
                .url("http://mydomain.com/sendData")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
