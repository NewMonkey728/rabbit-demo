package com.qy.rabbitdemo.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Serializable;

/**
 * RabbitMQ连接工具类
 * @author QY
 * @date 2021-01-1512:55
 **/
public class RabbitUtil {

    public static Connection getConnection(){
        try {
            //Rabbit连接工厂来创建连接
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setPort(5672);
            connectionFactory.setHost("10.36.137.161");
            connectionFactory.setPassword("admin");
            connectionFactory.setUsername("admin");
            connectionFactory.setVirtualHost("/test");
            return connectionFactory.newConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
