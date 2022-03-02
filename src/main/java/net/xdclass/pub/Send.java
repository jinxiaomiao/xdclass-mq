package net.xdclass.pub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author jinxm
 * @date 2022-03-01
 */

public class Send {

    private final static String EXCHANGE_NAME = "exchange_fanout";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/dev");
        factory.setPort(5672);

        //JDK7语法，自动关闭，创建连接
        try (Connection connection = factory.newConnection();
             //创建信道
             Channel channel = connection.createChannel()) {

            //绑定交换机，fanout扇形，即广播
             channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

             String msg = "rabbitmq测试";
             channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes(StandardCharsets.UTF_8));

            System.out.println("广播消息发送成功");


        }
    }
}