package com.spy.rabbitmq.listener;

import com.spy.rabbitmq.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2017-03-23 14:04
 */
@Component
@Slf4j
public class MessageListener {

    @RabbitListener(queues = RabbitmqConfig.DEFAULT_QUEUE)
    public void receiveMsg(final Message msg) {
        log.debug("msg is {}", msg.toString());
    }

//    @RabbitListener(queues = "")
//    public void receiveMsg2(final Message msg) {
//        log.debug("msg is {}", msg.toString());
//    }

}
