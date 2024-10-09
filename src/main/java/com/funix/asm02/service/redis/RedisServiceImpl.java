package com.funix.asm02.service.redis;

import com.funix.asm02.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService{
    private RedisTemplate<String, User> redisTemplate;
    @Autowired
    public RedisServiceImpl(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveUser(String email, User user) {
        redisTemplate.opsForValue().set(email, user);
    }

    public User getUser(String email) {
        return redisTemplate.opsForValue().get(email);
    }

}
