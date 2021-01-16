package com.qy.rabbitdemo.routing;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

public class Publisher {
    @Test
    public void pulibsher() throws Exception {
        //1创建链接
        Connection connection = RabbitUtil.getConnection();
        //2创建通道
        Channel channel = connection.createChannel();
        //3 声明交换机 BuiltinExchangeType.DIRECT=>路由模式
        channel.exchangeDeclare("routing-exchange",BuiltinExchangeType.DIRECT);
        //4 绑定队列   ERROR
        //将消息发布到交换机中，通过不同的路由键绑定队列

        channel.queueBind("q1-routing-queue","routing-exchange","ERROR");
        //INFO 路由键
        channel.queueBind("q2-routing-queue","routing-exchange","INFO");
        channel.queueBind("q2-routing-queue","routing-exchange","SUCCESS");

        //发布消息` 同时指定路由的规则,这里指定了三种规则，分别是ERROR，INFO，SUCCESS
        channel.basicPublish("routing-exchange","ERROR",null,"ERROR".getBytes());
        channel.basicPublish("routing-exchange","INFO",null,"INFO".getBytes());
        channel.basicPublish("routing-exchange","SUCCESS",null,"SUCCESS".getBytes());

        //关闭资源
        channel.close();
        connection.close();
    }
}
