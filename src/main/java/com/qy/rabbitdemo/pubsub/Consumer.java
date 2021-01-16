package com.qy.rabbitdemo.pubsub;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author QY
 * @date 2021-01-1612:26
 **/
public class Consumer {
    @Test
    public void consumer() throws IOException, TimeoutException {
        //创建连接
        Connection connection = RabbitUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建队列
        //参数1：queue - 指定队列的名称
        //参数2：durable - 当前队列是否需要持久化（true）
        //参数3：exclusive - 是否排外（conn.close() - 当前队列会被自动删除，当前队列只能被一个消费者消费）
        //参数4：autoDelete - 如果这个队列没有消费者在消费，队列自动删除
        //参数5：arguments - 指定当前队列的其他信息
        channel.queueDeclare("q1-queue",true,false,false,null);
        //设置每次只能消费1次
        channel.basicQos(1);
        //监听队列
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费q1的消息："+new String(body,"utf-8"));

                //设置手动应答，当接收到消息的时候才通知rabbit，消息已被消费
                channel.basicAck(envelope.getDeliveryTag(),false);

            }
        };
        //5消费回调 设置关闭自动应答 手动通知
        channel.basicConsume("q1-queue",false,defaultConsumer);
        //关闭
        System.out.println("消费消息：");
        System.in.read();
        channel.close();
        connection.close();
    }
}
