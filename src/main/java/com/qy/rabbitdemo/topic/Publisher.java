package com.qy.rabbitdemo.topic;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

public class Publisher {
    @Test
    public void pulibsher() throws Exception {
        //1创建链接
        Connection con = RabbitUtil.getConnection();
        //2创建通道
        Channel channel = con.createChannel();

        //3 声明交换机 BuiltinExchangeType.TOPIC=>通配符模式
        //生产者创建Topic的exchange并且绑定到队列中，这次绑定可以通过*和#关键字，
        // 对指定RoutingKey内容，编写时注意格式 xxx.xxx.xxx去编写， \* -> 一个xxx，
        // 而# -> 代表多个xxx.xxx，在发送消息时，指定具体的RoutingKey到底是什么。
        channel.exchangeDeclare("topic-exchange", BuiltinExchangeType.TOPIC);

        //4 绑定队列  动物相关  <speed><color><type>
        //不同的消费这关心的事情不一样   *代表匹配1个   # 代表匹配1个或多个
        //c1 *.red.* 值关心红色的动物,不关心他们是什么动物或者是什么速度的
        //c2 fast.#  只关心速度快的动物,其他不关心
        //c2 *.*.cat 猫奴其他的不关心
        //  sys.customer.add
        //  sys.customer.update
        //  sys.customer.del
        //  sys.customer.*
        //  index.item.show
        //  *.item.*
        //  sys.#
        channel.queueBind("q1-topic-queue", "topic-exchange", "*.red.*");
        channel.queueBind("q2-topic-queue", "topic-exchange", "fast.#");
        channel.queueBind("q2-topic-queue", "topic-exchange", "*.*.cat");
        //发布消息`
        channel.basicPublish("topic-exchange", "fast.red.dog",null,"快红狗".getBytes());
        channel.basicPublish("topic-exchange", "slow.black.cat",null,"慢黑猫".getBytes());
        channel.basicPublish("topic-exchange", "slow.green.bird",null,"慢绿鸟".getBytes());
        channel.basicPublish("topic-exchange", "slow.red.pig",null,"慢红猪".getBytes());
        //关闭资源
        channel.close();
        con.close();
    }
}
