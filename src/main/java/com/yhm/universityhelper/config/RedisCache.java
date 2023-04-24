package com.yhm.universityhelper.config;

import com.yhm.universityhelper.util.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class RedisCache implements Cache {
    // 读写公平，锁整个Cache，读多写少时性能好，阻塞写，不阻塞读
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private RedisTemplate<String, Object> redisTemplate;

    private final String id;
    public RedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        log.info("MybatisRedisCache:id=" + id);
        this.id = id;
    }
    
    public RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate<String, Object>) ApplicationContextUtils.getBean("redisTemplate");
        }
        return redisTemplate;
    }
    
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.debug("redis set: key=" + key + ",value=" + value);
        if (value != null) {
            getRedisTemplate().opsForValue().set(key.toString(), value);
        }
    }

    @Override
    public Object getObject(Object key) {
        log.debug("redis get: key=" + key);
        try {
            if (key != null) {
                return getRedisTemplate().opsForValue().get(key.toString());
            }
        } catch (Exception e) {
            log.error("redis get error: key=" + key, e);
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        log.debug("redis remove: key=" + key);
        if (key != null) {
            getRedisTemplate().unlink(key.toString());
        }
        return null;
    }

    @Override
    public void clear() {
        log.debug("redis clear");
        Set<String> keys = getRedisTemplate().keys("*:" + this.id + "*");
        if (keys != null && keys.size() > 0) {
            getRedisTemplate().executePipelined((RedisCallback<Object>)connection -> {
                for (String key : keys) {
                    connection.unlink(key.getBytes());
                }
                return null;
            });
        }
    }

    @Override
    public int getSize() {
        Long size = getRedisTemplate().execute(RedisServerCommands::dbSize);
        return Objects.requireNonNull(size).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}
