package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.MyInterceptor;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MyRpcfxInvoker {

    private RpcfxResolver resolver;

    public MyRpcfxInvoker(RpcfxResolver resolver){
        this.resolver = resolver;
    }

    public Object invoke(RpcfxRequest request)  {
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射
        // 实现方式：将带有MyService注解的类实例化之后放到Map,获取实例从Map获取
        Object service = resolver.resolve(serviceClass);//this.applicationContext.getBean(serviceClass);

        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            return JSON.toJSONString(result, SerializerFeature.WriteClassName);
        } catch ( IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            //return response;
            return null;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
