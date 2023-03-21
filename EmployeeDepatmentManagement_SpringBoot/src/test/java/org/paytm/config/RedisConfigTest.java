package org.paytm.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfiguration
@ExtendWith(MockitoExtension.class)
public class RedisConfigTest {


    @InjectMocks
    private RedisConfig redisConfig;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void connectionFactory_success() {


        RedisConnectionFactory connectionFactory = this.redisConfig.connectionFactory();
        Assertions.assertNotNull(connectionFactory);

        // Verify the executor properties
        //assertEquals(30, connectionFactory.getConnection().getClientName());
    }

    @Test
    public void jedisPoolConfig_success() {

        RedisCacheConfiguration redisCacheConfiguration = this.redisConfig.cacheConfiguration();
        Assertions.assertNotNull(redisCacheConfiguration);

        // Verify the executor properties
        assertEquals(Duration.ofMinutes(50), redisCacheConfiguration.getTtl());
    }
}
