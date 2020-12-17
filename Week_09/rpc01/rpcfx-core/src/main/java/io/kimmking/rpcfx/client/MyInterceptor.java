package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class MyInterceptor implements MethodInterceptor {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    private List<String> list = new LinkedList<>();
    private Class<?> klass;
    private String url;

    public MyInterceptor(Class<?> klass, String url) {
        this.klass = klass;
        this.url = url;
        list.add("toString");
    }

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //不被拦截
        if (list.contains(method.getName())) {
            return method.invoke(o, objects);
        }

        //return method.getName();
        //        Object obj = method.invoke(o,objects);
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(klass.getName());
        request.setMethod(method.getName());
        request.setParams(objects);
        Object response = post(request, this.url);
        return response;
    }

    private Object post(RpcfxRequest req, String url) throws IOException {

        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);
        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String result = client.newCall(request).execute().body().string();
        System.out.println("resp json: " + result);
        return JSON.parse(result);
    }
}
