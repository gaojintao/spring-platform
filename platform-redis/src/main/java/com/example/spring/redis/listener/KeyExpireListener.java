package com.example.spring.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @ClassName KeyExpireListener
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/19 15:15
 * @Version 1.0
 **/

@Component
public class KeyExpireListener extends KeyExpirationEventMessageListener {

    final static Logger logger = LoggerFactory.getLogger(KeyExpireListener.class);

    // 通过构造函数注入 RedisMessageListenerContainer 给 KeyExpirationEventMessageListener
    public KeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void doHandleMessage(Message message) {

        // 过期的 key
        byte[] body = message.getBody();

        // 消息通道
        byte[] channel = message.getChannel();

        logger.info("message = {}, channel = {}", new String(body), new String(channel));
    }
}