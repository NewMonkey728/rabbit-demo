package com.qy.rabbitdemo.hello;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author QY
 * @date 2021-01-1513:16
 **/
public class Consumer {
    @Test
    public void consume() throws IOException, TimeoutException {
        //1，创建连接
        Connection connection = RabbitUtil.getConnection();

        //2，创建通道
        Channel channel = connection.createChannel();
        //3，创建队列
        //参数1：queue - 指定队列的名称
        //参数2：durable - 当前队列是否需要持久化（true）
        //参数3：exclusive - 是否排外（conn.close() - 当前队列会被自动删除，当前队列只能被一个消费者消费）
        //参数4：autoDelete - 如果这个队列没有消费者在消费，队列自动删除
        //参数5：arguments - 指定当前队列的其他信息
        channel.queueDeclare("hello",true, false,false,null);

        //4，创建监听
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接受到消息是:"+new String(body,"utf-8"));
            }
        };
        //5，消费回调
            channel.basicConsume("hello",true,defaultConsumer);

        //6，关闭资源
        System.out.println("消费消息");
        System.in.read();
        channel.close();
        connection.close();
    }

    @Test
    public void test() throws IOException, TimeoutException {
        //1,创建连接
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare("hello",true,false,false,null);

        //创建监听
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收的消息是："+new String(body,"utf-8"));
            }
        };
        //消息回调
        //参数1：queue - 指定消费哪个队列
        //参数2：autoAck - 指定是否自动ACK （true，接收到消息后，会立即告诉RabbitMQ）
        //参数3：consumer - 指定消费回调
        channel.basicConsume("hello",true,defaultConsumer);
        //关闭资源
        System.out.println("消费信息");
        System.in.read();
        channel.close();
        connection.close();

    }

}
