package utils;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface Containers {

    @Container
    @ServiceConnection
    RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:7.2.4-alpine"));
}
