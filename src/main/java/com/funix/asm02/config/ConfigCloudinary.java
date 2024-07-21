package com.funix.asm02.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dms5ypv4r");
        config.put("api_key", "132835166733769");
        config.put("api_secret", "dnTRkUekh5SkOGYapPc7nRY-xdQ");
        return new Cloudinary(config);
    }
}
