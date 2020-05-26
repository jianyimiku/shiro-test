package com.cslg.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-26 12:30
 **/
@Component
public class JedisUtil {

    @Autowired
    private JedisPool jedisPool;


    private Jedis getResource() {
        return jedisPool.getResource();
    }


    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();

        try {
            jedis.set(key, value);
            return value;
        } finally {
            jedis.close();
        }

    }

    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, i);
        }finally {
            jedis.close();
        }
    }

    public byte[] get(byte[] key){
        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        }finally {
            jedis.close();
        }
    }

    public void delete(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((shiro_session_prefix+"*").getBytes());
        }finally {
            jedis.close();
        }
    }
}
