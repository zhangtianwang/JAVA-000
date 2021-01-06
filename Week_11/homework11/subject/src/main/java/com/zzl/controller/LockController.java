package com.zzl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/lock")
public class LockController {


    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 模拟分布式锁的调用
     * @param key
     * @param val
     * @return
     */
    @GetMapping("/distributedLockTest")
    public String distributedLockTest(String key, String val) {
        if (lock(key, val, 10000)) {
            return (String) redisTemplate.opsForValue().get(key);
        } else {
            return "分布式锁失败";
        }
    }

    /**
     * 分布式锁
     *
     * @param key
     * @param val
     * @param expireTime
     * @return
     */
    private boolean lock(String key, String val, long expireTime) {
        if (redisTemplate.opsForValue().setIfAbsent(key, val, expireTime, TimeUnit.MILLISECONDS)) {
            return true;
        }
        long start = System.currentTimeMillis();
        while (true) {
            long time = System.currentTimeMillis() - start;
            if (time >= expireTime) {
                break;
            }
            if (redisTemplate.opsForValue().setIfAbsent(key, val, expireTime, TimeUnit.MILLISECONDS)) {
                return true;
            }
        }
        return false;
    }

}
