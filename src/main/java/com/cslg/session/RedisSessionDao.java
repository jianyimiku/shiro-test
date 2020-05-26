package com.cslg.session;

import com.cslg.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-26 12:26
 **/
@Repository("redisSessionDao")
public class RedisSessionDao extends AbstractSessionDAO {
    @Autowired
    private JedisUtil jedisUtil;

    private final String shiro_session_prefix = "miku-session:";

    private byte[] getKey(String key) {
        return (shiro_session_prefix + key).getBytes();
    }

    public void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            //序列化以后存储
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }

    @Override
    //创建session
    protected Serializable doCreate(Session session) {
        System.out.println("create");
        //生成一个新的sessionId, 并将它应用到session实例
        Serializable sessionId = generateSessionId(session);
        System.out.println(sessionId);
        System.out.println(sessionId.getClass());
        //捆绑sessionId 和 session
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    //获取session
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        //反序列化
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.delete(key);

    }

    @Override
    //获取到指定的存活的session
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(shiro_session_prefix);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(sessions)){
            return sessions;
        }
        for (byte[] key:keys){
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
