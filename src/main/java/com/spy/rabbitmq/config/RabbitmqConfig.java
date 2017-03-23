package com.spy.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2017-03-16 15:20
 */

@Configuration
@Slf4j
public class RabbitmqConfig {
    public static final String EXCHANGE = "ex-spy-test";
    public static final String ROUTINGKEY = "normal";
    public static final String DEFAULT_QUEUE = "qu-spy-default";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("docker1.cd121.cc:5672");
        connectionFactory.setUsername("spy");
        connectionFactory.setPassword("shipengyan");
        connectionFactory.setVirtualHost("vh-spy");
        connectionFactory.setPublisherConfirms(true); //必须要设置


        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //rabbitTemplate.setChannelTransacted(true); // 事务模式,必须在Listener中也要启用

        //
        return rabbitTemplate;
    }

    //    @Bean
    public RabbitTransactionManager rabbitTransactionManager() {
        RabbitTransactionManager rabbitTransactionManager = new RabbitTransactionManager(connectionFactory());
        return rabbitTransactionManager;
    }


    /**
     * 针对消费者配置
     * 1. 设置交换机类型 <p>
     * 2. 将队列绑定到交换机<p>
     * <p>
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(DEFAULT_QUEUE, true); //队列持久

    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(ROUTINGKEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new FastJsonMessageConverter();
    }


    @Bean(name = "simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory());

        containerFactory.setMaxConcurrentConsumers(1);
        containerFactory.setConcurrentConsumers(1);

        containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerFactory.setMessageConverter(jsonMessageConverter());

        return containerFactory;
    }
//   //这个太简单了
//    @Bean
//    public SimpleMessageListenerContainer messageContainer() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueues(queue());
//        container.setExposeListenerChannel(true);
//        container.setMaxConcurrentConsumers(1);
//        container.setConcurrentConsumers(1);
//
//        //这个与confirmSelect模式只能选择一种
////        container.setChannelTransacted(true);
////        container.setTransactionManager(rabbitTransactionManager());
//
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
//        container.setMessageConverter(jsonMessageConverter()); //json
//
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                byte[] body = message.getBody();
//                log.debug("receive msg : {}", new String(body));
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
//            }
//        });
//        return container;
//    }

}
