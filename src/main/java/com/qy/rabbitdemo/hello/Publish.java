package com.qy.rabbitdemo.hello;


import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.beans.Transient;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 一个生产者，一个和默认交换机，一个队列，一个消费者
 * @author QY
 * @date 2021-01-1513:12
 **/
public class Publish {
    @Test
   public void publish() throws IOException, TimeoutException {
       //1,创建连接
       Connection connection = RabbitUtil.getConnection();
       //2，创建通道
       Channel channel = connection.createChannel();
       //3，发送消息
       String msg="hello   >-<";
       // 参数1：指定交换机exchange，使用""。
       // 参数2：指定路由的规则，使用具体的队列名称。
       // 参数3：指定传递的消息所携带的properties，使用null。
       // 参数4：指定发布的具体消息，byte[]类型
        //调用basicPublish的API
        channel.basicPublish("","hello",null,msg.getBytes());
        System.out.println("消息已发送！！！");
        //4，释放资源
        channel.close();
        connection.close();
   }

    @Test
    public void test() throws IOException, TimeoutException {
        //1，创建连接对象
        Connection connection = RabbitUtil.getConnection();
        //2,然后创建通道
        Channel channel = connection.createChannel();
        //然后发送消息，先准备消息
        String msg="hello----------";

        //然后通过通道调用basicPublish的API来完成发送消息
        channel.basicPublish("","hello",null,msg.getBytes());
        System.out.println("消息已经发送");

        //最后试放资源
        channel.close();
        connection.close();

    }
}
