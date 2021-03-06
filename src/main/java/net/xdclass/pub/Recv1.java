package net.xdclass.pub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jinxm
 * @date 2022-03-01
 */

public class Recv1 {

    private final static String EXCHANGE_NAME = "exchange_fanout";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/dev");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //绑定交换机，fanout扇形，即广播
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //获取队列
        String queueName = channel.queueDeclare().getQueue();

        //绑定交换机和队列，fanout交换机不用routeingKey
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("body="+new String(body,"utf-8"));

                //手工确认消息消费，不是多条确认
                channel.basicAck(envelope.getDeliveryTag(),false);

            }
        };

        //消费,关闭消息消息自动确认,重要
        channel.basicConsume(queueName,false,consumer);

    }
}
