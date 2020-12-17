package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.client.MyInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcfxClientApplication {

    // 二方库
    // 三方库 lib
    // nexus, userserivce -> userdao -> user
    //

    public static void main(String[] args) {

        try {
            //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
            org.springframework.cglib.proxy.Enhancer enhancer = new org.springframework.cglib.proxy.Enhancer();
            //设置目标类的字节码文件
            enhancer.setSuperclass(UserService.class);
            enhancer.setCallback(new MyInterceptor(UserService.class, "http://localhost:8081/"));
            UserService proxy = (UserService) enhancer.create();
            User res = proxy.findById(1);
            System.out.println("打印方法名：" + res);

//			User user = proxy.findById(1);
//			System.out.println("find user id=1 from server: " + user.getName());
//
//			OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
//			Order order = orderService.findOrderById(1992129);
//			System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }

        // 新加一个OrderService

        SpringApplication.run(RpcfxClientApplication.class, args);
    }


}
