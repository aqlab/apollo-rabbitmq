package com.spy.rabbitmq.service;

import com.spy.rabbitmq.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2017-03-18 14:46
 */
@Slf4j
public class MQServiceTest extends BaseTest {

    @Autowired
    private MQService mqService;


    @Test
    public void producerTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            mqService.sendMsg("hi, hello world," + i);

            Thread.sleep(1000 * 10);
        }
    }

}