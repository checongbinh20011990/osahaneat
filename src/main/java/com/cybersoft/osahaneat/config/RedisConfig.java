package com.cybersoft.osahaneat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

//    Tạo connection kết nối tới hệ thống redis
    @Bean
    public LettuceConnectionFactory redisConnection(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host,port);
////        config.setDatabase(0); Chỉ định database sử dụng trong redis
////        config.setUsername(""); Thông tin tài khoản đăng nhập vào redis
////        config.setPassword(""); Thông tin mật khẩu ứng với tài khoản đăng nhập vào redis
        config.setHostName(host);
        config.setPort(port);

        return new LettuceConnectionFactory(config);
    }

//    Tạo template để thực hiện thêm xóa sửa trên redis
    @Bean
    @Primary
    public RedisTemplate<Object,Object> redisTemplate(LettuceConnectionFactory redisConnection){
        RedisTemplate<Object,Object> template = new RedisTemplate<>();
        System.out.println("kiemtra " + redisConnection.getHostName() + " - " + redisConnection.getPort());
        template.setConnectionFactory(redisConnection);

        return template;
    }

}
