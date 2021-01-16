package com.qy.rabbitdemo.work;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author QY
 * @date 2021-01-1610:10
 **/
public class Consumer  {

        @Test
    public void consumer() throws IOException, TimeoutException {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        //创建队列
        //参数1：queue - 指定队列的名称
        //参数2：durable - 当前队列是否需要持久化（true）
        //参数3：exclusive - 是否排外（conn.close() - 当前队列会被自动删除，当前队列只能被一个消费者消费）
        //参数4：autoDelete - 如果这个队列没有消费者在消费，队列自动删除
        //参数5：arguments - 指定当前队列的其他信息
        channel.queueDeclare("work",true,false,false,null);
        //设置每次只监听一次消息，指该消费者在接收到队列里的消息
        // 但没有返回确认结果之前,它不会将新的消息分发给它。
        channel.basicQos(1);
        //开启监听
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收的消息是："+new String(body,"utf-8"));

                //设置消息接收后，手动通知/应答  告知rabbit消息已被消费
                channel.basicAck(envelope.getDeliveryTag(),false);

            }
        };
        //监听之后就回调消息,,第二个参数关闭自动应答，手动通知
        channel.basicConsume("work",false,defaultConsumer);

        System.out.println("消费消息：");
        System.in.read();
        channel.close();
        connection.close();

    }
}
