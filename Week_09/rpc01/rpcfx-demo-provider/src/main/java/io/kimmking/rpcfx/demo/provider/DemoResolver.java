package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.api.RpcfxResolver;
import io.my.MyServiceContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

//public class DemoResolver implements RpcfxResolver, ApplicationContextAware {
public class DemoResolver implements RpcfxResolver {

    @Override
    public <T> T resolve(String serviceClass) {
        //从容器中获取对应service实现类的实例
        T service =  (T)MyServiceContainer.getMap().get(serviceClass);
        return service;
    }
}
