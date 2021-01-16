package com.qy.rabbitdemo.pubsub;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author QY
 * @date 2021-01-1612:26
 **/
public class Publish {
    @Test
    public void publish() throws IOException, TimeoutException {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明交换机   BuiltinExchangeType.FANOUT 发布-订阅模式
        channel.exchangeDeclare("pubsub-exchange", BuiltinExchangeType.FANOUT);
        //交换机跟队列 绑定
        //参数1：队列名
        //参数2：交换机
        channel.queueBind("q1-queue","pubsub-exchange","");
        channel.queueBind("q2-queue","pubsub-exchange","");
        //发送消息
        //模拟批量发布消息
        for (int i = 0; i <10;i++){
            String msg="hello------>"+i;
            // 参数1：指定交换机exchange，如果没有使用""。
            // 参数2：指定路由的规则，使用具体的队列名称。
            // 参数3：指定传递的消息所携带的properties，使用null。
            // 参数4：指定发布的具体消息，byte[]类型
            //调用basicPublish的API
            channel.basicPublish("pubsub-exchange","",null,msg.getBytes());
        }

        //关闭资源
        channel.close();
        connection.close();

    }
}
