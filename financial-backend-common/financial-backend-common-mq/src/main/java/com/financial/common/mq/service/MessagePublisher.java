package com.financial.common.mq.service;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息生产者
 * @author xinyi
 */
@Service
public class MessagePublisher {

    @Resource
    private RabbitTemplate rabbitTemplate;
    
    public void publishNotification(String userId, String content) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "NOTIFICATION");
        message.put("timestamp", System.currentTimeMillis());
        message.put("content", content);
        
        rabbitTemplate.convertAndSend("finance.exchange",
            "finance.routingKey",
            message,
            m -> {
                m.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return m;
            });
    }
}