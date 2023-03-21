package org.paytm.config;



import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Configuration
public class RedisConfig {

    //connections with configurations=>
    @Bean
    public RedisConnectionFactory connectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = null;
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.usePooling().poolConfig(this.jedisPoolConfig());

        String listNodes = "localhost";


        log.info("RedisClusterConfig::connectionFactory  initializing RedisClusterConfiguration");
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(listNodes);
        redisStandaloneConfiguration.setDatabase(0);
        jedisConnectionFactory =
                new JedisConnectionFactory(
                        redisStandaloneConfiguration, jedisClientConfiguration.build());
        return jedisConnectionFactory;
    }


    private JedisPoolConfig jedisPoolConfig() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(18);
        jedisPoolConfig.setMinIdle(2);
        return jedisPoolConfig;
    }

    //template to access the redis server
    //one can use annotations directly instead of the template
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){   //used for calling third party's rest APIs - (<key, value>)
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer());   //recheck
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true); //to avoid data overwriting
        //template.afterPropertiesSet();  //checkout
        return template;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {   //deleting previous key of the cache after a duration of 5 mins
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(50));
    }

    @Bean
    public CacheManager cacheManager(final RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration())
                .enableStatistics()
                .build();
    }
}
