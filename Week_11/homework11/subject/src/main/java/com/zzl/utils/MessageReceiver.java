package com.zzl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    @Autowired
    private StringRedisTemplate template;

    public void receive(String message) {
        System.out.print("接收到消息：" + message);
    }

}
