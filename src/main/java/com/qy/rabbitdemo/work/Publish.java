package com.qy.rabbitdemo.work;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Handler;

/**
 * @author QY
 * @date 2021-01-1610:40
 **/
public class Publish {

    @Test
    public void publish() throws IOException, TimeoutException {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        //循环 模拟批量发布消息
        for (int i = 0; i < 10;i++){
            String msg="hello----->"+i;
            channel.basicPublish("","work",null,msg.getBytes());
        }
        channel.close();
        connection.close();

    }
}
