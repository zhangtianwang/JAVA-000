package com;
import okhttp3.*;

import java.io.IOException;

public class HttpClientUtils {


    public static void main(String[] args){
        try {
          String msg =  httpGet("http://localhost:8801");
          System.out.println(msg);
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }

    public static String httpGet(String url) throws IOException {
        OkHttpClient httpClient = new okhttp3.OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
    }
}



