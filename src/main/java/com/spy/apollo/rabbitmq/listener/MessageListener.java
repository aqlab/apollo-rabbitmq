package com.spy.apollo.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.spy.apollo.rabbitmq.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2017-03-23 14:04
 */
@Slf4j
@Component
public class MessageListener {

    @RabbitListener(queues = RabbitmqConfig.DEFAULT_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    public void receiveMsg(final Message msg,
                           final Channel channel,
                           @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
//        log.debug("msg is {}", msg.toString());

        log.debug("msg object is {}", msg);
        channel.basicAck(tag, false); //确认消息成功消费
    }


}
