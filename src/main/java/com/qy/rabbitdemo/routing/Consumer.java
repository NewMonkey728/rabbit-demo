package com.qy.rabbitdemo.routing;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer {
    @Test
    public void consumer() throws Exception {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare("q1-routing-queue", true
                ,false,false,null);
        //设置每次只消费1个消息
        channel.basicQos(1);
        //监听
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收到的消息："+new String(body,"utf-8"));
                //设置手动应答，当接收到消息之后通知rabbit，告诉它消息已被接收
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //5消费回调 设置关闭自动应答 手动通知
        channel.basicConsume("q1-routing-queue",false,defaultConsumer);
        //关闭
        System.out.println("消息接收：");
        System.in.read();
        channel.close();
        connection.close();


    }
}
