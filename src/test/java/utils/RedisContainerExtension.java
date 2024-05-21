package utils;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class RedisContainerExtension implements BeforeAllCallback, AfterAllCallback {

    @Container
    private static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7.2.4-alpine"));

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        redisContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        redisContainer.start();
    }
}
