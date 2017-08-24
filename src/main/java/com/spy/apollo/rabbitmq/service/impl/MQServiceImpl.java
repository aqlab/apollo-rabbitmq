package com.spy.apollo.rabbitmq.service.impl;

import com.spy.apollo.rabbitmq.config.RabbitmqConfig;
import com.spy.apollo.rabbitmq.service.MQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2017-03-18 14:42
 */
@Service
public class MQServiceImpl implements MQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    /**
     * 发送消息
     *
     * @param content
     */
    @Override
    public void sendMsg(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, RabbitmqConfig.ROUTINGKEY, content, correlationId);
    }

    @Override
    public void sendMsg(Object object) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, RabbitmqConfig.ROUTINGKEY, object, correlationId);
    }

    @Transactional
    public void sendMsgExceptionByTx(String content) {

        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, RabbitmqConfig.ROUTINGKEY, content, correlationId);
        //DB op
        int a = 1 / 0;
    }
}
