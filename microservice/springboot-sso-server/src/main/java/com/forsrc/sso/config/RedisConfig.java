package com.forsrc.sso.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Duration;
import java.util.*;

@Configuration
@EnableCaching(mode = AdviceMode.PROXY)
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.ON_SAVE)
@ConfigurationProperties(value = "spring.cache")
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;


    private List<CacheTtl> cacheTtls = new ArrayList<>();


    public static class CacheTtl {
        private String cacheName;
        private int ttl;

        public int getTtl() {
            return ttl;
        }

        public String getCacheName() {
            return cacheName;
        }

        public void setCacheName(String cacheName) {
            this.cacheName = cacheName;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }
    }


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        // redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        // redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        return factory;
    }


    @Bean
    CacheManager cacheManager() {
        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory())
                .withInitialCacheConfigurations(getRedisCacheConfigurationMap())
                .build();
        return cacheManager;
    }

    @Bean
    public RedisTemplate<Object, Object> redisCacheTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    @Primary
    public FindByIndexNameSessionRepository getSessionRepository() {
        RedisTemplate<Object, Object> redisTemplate = redisCacheTemplate();
        return new RedisOperationsSessionRepository(redisTemplate);
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        if (cacheTtls == null) {
            return Collections.emptyMap();
        }
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        for (CacheTtl cacheTtl : cacheTtls) {
            redisCacheConfigurationMap.put(cacheTtl.getCacheName(), redisCacheConfigurationTtl(cacheTtl.getTtl()));
        }
        return redisCacheConfigurationMap;
    }


    private static RedisCacheConfiguration redisCacheConfigurationTtl(int seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }


    public List<CacheTtl> getCacheTtls() {
        return cacheTtls;
    }

    public void setCacheTtls(List<CacheTtl> cacheTtls) {
        this.cacheTtls = cacheTtls;
    }
}
