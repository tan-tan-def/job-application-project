package com.funix.asm02.service.redis;

import com.funix.asm02.entity.User;

public interface RedisService {
    void saveUser(String email, User user);
    User getUser(String email);
}
