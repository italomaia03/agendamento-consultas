package utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperties {

    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.host}")
    private String redisHost;

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisHost() {
        return redisHost;
    }
}
