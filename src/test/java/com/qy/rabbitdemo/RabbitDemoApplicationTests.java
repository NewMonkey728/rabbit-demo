package com.qy.rabbitdemo;

import com.qy.rabbitdemo.utils.RabbitUtil;
import com.rabbitmq.client.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.io.IOException;

@SpringBootTest
class RabbitDemoApplicationTests {

    @Test
    void contextLoads() throws IOException {
        Connection connection = RabbitUtil.getConnection();

        //阻塞
        System.in.read();
        connection.close();


    }

}
