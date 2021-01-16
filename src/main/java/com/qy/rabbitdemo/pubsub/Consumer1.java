package com.qy.rabbitdemo.pubsub;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.concurrent.TimeoutException;

/**
 * @author QY
 * @date 2021-01-1612:26
 **/
public class Consumer1 {
    @Test
    public void consumer1() throws IOException, TimeoutException {

        //创建连接
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("q2-queue",true,false,false,null);
        //设置每次消费只消费1次
        channel.basicQos(1);
        //监听
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费q2的消息："+new String(body,"utf-8"));
                //设置手动应答，当接收到消息的时候才通知rabbit，消息已被收到
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //5消费回调 设置关闭自动应答 手动通知
        channel.basicConsume("q2-queue",false,defaultConsumer);
        System.out.println("接收消息");
        System.in.read();
        channel.close();
        connection.close();
    }
}
