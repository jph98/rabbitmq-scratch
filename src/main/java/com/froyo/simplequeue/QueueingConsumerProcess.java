package com.froyo.simplequeue;

import java.io.IOException;
import java.util.UUID;

import com.froyo.common.Ack;
import com.froyo.common.Queues;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class QueueingConsumerProcess {

    public static final String URL = "http://requestb.in/1elljh51";

    public QueueingConsumerProcess() {

    }

    public static void main(String[] argv) throws Exception {

        QueueingConsumerProcess server = new QueueingConsumerProcess();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(Queues.QUEUE_NAME, false, false, false, null);

        System.out.println(" [*] Waiting for messages on " + Queues.QUEUE_NAME + " to exit press CTRL+C");

        // We want to explictly ACK messages
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(Queues.QUEUE_NAME, Ack.NO_AUTO_ACK, consumer);

        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

        basicAckSend(server, channel, delivery);
    }

    private static void basicAckSend(QueueingConsumerProcess server, Channel channel, QueueingConsumer.Delivery delivery) throws IOException {


        System.out.println("Waiting on message: [" + new String(delivery.getBody()) + "]");

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
        }

        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);


    }
}
