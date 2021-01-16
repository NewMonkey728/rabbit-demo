package com.qy.rabbitdemo.routing;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class Consumer2 {
    @Test
    public void consumer() throws Exception {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("q2-routing-queue", true
                ,false,false,null);
        //设置每次只消费1个
        channel.basicQos(1);
        //监听
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收到的消息："+new String(body,"utf-8"));
                //设置接受到消息之后手动应答，告知rabbit消息已被消费
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //5消费回调 设置关闭自动应答 手动通知
        channel.basicConsume("q2-routing-queue",false,defaultConsumer);
        //关闭
        System.out.println("接收消息：");
        System.in.read();
        //关闭资源
        channel.close();
        connection.close();

    }
}
