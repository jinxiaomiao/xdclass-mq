package net.xdclass.topic;

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

    private final static String EXCHANGE_NAME = "exchange_topic";

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

            //绑定交换机，topic交换机
             channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

             String error = "我是订单服务的error日志";
             String info = "我是订单服务的info日志";
             String debug = "我是商品服务的debug日志";

             channel.basicPublish(EXCHANGE_NAME, "order.log.error", null, error.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "order.log.info", null, info.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "product.log.debug", null, debug.getBytes(StandardCharsets.UTF_8));

            System.out.println("topic消息发送成功");


        }
    }
}