package com.zzl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pubsub")
public class PubSubController {

    @Autowired
    private StringRedisTemplate template;

    @GetMapping("publish")
    public String publish(String content){
        template.convertAndSend("orderChannel",content);
        return "成功";
    }

}
