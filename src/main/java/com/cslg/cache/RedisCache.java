package com.cslg.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.Set;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-26 15:04
 **/
@Component
//Cache 实例
public class RedisCache<K, V> implements Cache<K, V> {
    @Autowired
    private JedisPool jedisPool;

    private final String cache_prefix = "miku-cache:";
    private V v;

    private byte[] getKey(K k) {
        if (k instanceof String) {
            return (cache_prefix + k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    @Override
    public V get(K k) throws CacheException {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] value = jedis.get(getKey(k));
            if (value != null) {
                return (V) SerializationUtils.deserialize(value);
            }
        } finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] key = getKey(k);
            byte[] value = SerializationUtils.serialize(v);
            jedis.set(key, value);
            jedis.expire(key,600);
        } finally {
            jedis.close();
        }
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] key = getKey(k);
            v = (V) SerializationUtils.deserialize(jedis.get(key));
            jedis.del(key);

        } finally {
            jedis.close();
        }
        return v;
    }

    @Override
    public void clear() throws CacheException {
        //不用重写
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
