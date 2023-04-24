package com.yhm.universityhelper.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.yhm.universityhelper.util.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class MybatisRedisCache implements Cache {
    // 读写公平，锁整个Cache，读多写少时性能好，阻塞写，不阻塞读
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private RedisTemplate<String, Object> redisTemplate;

    private final String id;

    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        log.info("MybatisRedisCache:id=" + id);
        this.id = id;
    }

    public int randomExpire() {
        return RandomUtil.randomInt(3600, 86400);
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate<String, Object>)ApplicationContextUtils.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (value != null) {
            getRedisTemplate().opsForValue().set(key.toString(), value, randomExpire());
        }
    }

    @Override
    public Object getObject(Object key) {
        try {
            if (key != null) {
                return getRedisTemplate().opsForValue().get(key.toString());
            }
        } catch (Exception e) {
            log.error("缓存出错");
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        if (key != null) {
            getRedisTemplate().unlink(key.toString());
        }
        return null;
    }

    @Override
    public void clear() {
        log.debug("清空缓存");
        Set<String> keys = getRedisTemplate().keys("*:" + this.id + "*");
        if (!CollectionUtil.isEmpty(keys)) {
            getRedisTemplate().unlink(keys);
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
