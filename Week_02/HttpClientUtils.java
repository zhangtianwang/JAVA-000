package com;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class HttpClientUtils {


    public static void main(String[] args){
        try {
            HttpClient client = new HttpClient();
            HttpMethod method = new GetMethod("http://localhost:8801");
            client.executeMethod(method);
            //释放连接
            method.releaseConnection();
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }
}



