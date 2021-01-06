package com.zzl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
public class CounterController {


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 初始化库存的数量
     *
     * @param key
     * @return
     */
    @GetMapping("/init")
    public String init(String key) {
        try {
            redisTemplate.opsForValue().set(key, "100");
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
        return "成功";
    }

    /**
     * 模拟减库存的操作
     *
     * @param key
     * @return
     */
    @GetMapping("/decreaseInventoryTest")
    public Long decreaseInventoryTest(String key, long num) {
        Long res = redisTemplate.opsForValue().decrement(key, num);
        //一直建库存，如果小于0，则加1
        if (res < 0) {
           res = redisTemplate.opsForValue().increment(key, num);
        }
        return res;
    }


}
