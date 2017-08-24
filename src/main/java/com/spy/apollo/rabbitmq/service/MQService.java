package com.spy.apollo.rabbitmq.service;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2017-03-18 14:42
 */
public interface MQService {

    /**
     * 发送消息
     *
     * @param content
     */
    public void sendMsg(String content);

    public void sendMsg(Object object);

    public void sendMsgExceptionByTx(String content);
}
