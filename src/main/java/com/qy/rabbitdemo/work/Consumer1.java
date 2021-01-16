package com.qy.rabbitdemo.work;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author QY
 * @date 2021-01-1610:41
 **/
public class Consumer1 {

    @Test
    public void consumer1() throws IOException, TimeoutException {
        //创建连接
        Connection connection = RabbitUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare("work",true,false,false,null);
        //设置每次只监听一次消息，该消费者在接收队列里的消息但没有返回确认之前，就不会将新的消息发送给它
        channel.basicQos(1);

        //监听
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收的消息："+new String(body,"utf-8"));
                //设置消费接收后，手动应答，告知rabbit消息已被消费
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //消息回调`
        System.out.println("消费消息：");
        channel.basicConsume("work",false,defaultConsumer);
        System.in.read();
        channel.close();
        connection.close();


    }
}
