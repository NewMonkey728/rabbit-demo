package com.qy.rabbitdemo.topic;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer2 {
    @Test
    public void consumer() throws Exception {
        //1创建链接
        Connection con = RabbitUtil.getConnection();
        //2 创建通道
        Channel channel = con.createChannel();
        //3创建队里
        channel.queueDeclare("q2-topic-queue", true
                ,false,false,null);
        //设置每次只消费1个消息
        channel.basicQos(1);
        //4监听队列
        DefaultConsumer dc = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("接受到的消息(1):"+new String(body,"utf-8"));
                //设置当消费消息后进行手动通知/应答 告知rabbit消息已被消费
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //5消费回调 设置关闭自动应答 手动通知
        channel.basicConsume("q2-topic-queue", false, dc);
        //6关闭
        System.out.println("消费消息");
        System.in.read();
        //关闭资源
        channel.close();
        con.close();
    }
}
