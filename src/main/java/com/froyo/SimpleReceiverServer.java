package com.froyo;

import java.io.IOException;
import java.util.UUID;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SimpleReceiverServer {

    public static final String URL = "http://requestb.in/1elljh51";

    public SimpleReceiverServer() {

    }

    public int sendMessage() {

        HttpClient client = new HttpClient();

        PostMethod post = new PostMethod(URL);
        NameValuePair[] data = {
                new NameValuePair("UUID", UUID.randomUUID().toString())
        };
        post.setRequestBody(data);

        try {
            return client.executeMethod(post);
        } catch (Exception e) {
            return 500;
        } finally {
            post.releaseConnection();
        }
    }

    public static void main(String[] argv) throws Exception {

        SimpleReceiverServer server = new SimpleReceiverServer();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(Queues.QUEUE_NAME, false, false, false, null);

        System.out.println(" [*] Waiting for messages on " + Queues.QUEUE_NAME + " to exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(Queues.QUEUE_NAME, true, consumer);

        while (true) {

            // Block until we get a message
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            try {
                // Wrap our operation in a transaction
                System.out.println("TX Start");
                channel.txSelect();
                String message = new String(delivery.getBody());
                System.out.println("\t[x] Consumed'" + message + "'");

                // Send this to our request bin instance
                server.sendMessage();

                System.out.println("Sent to request bin");

            } finally {
                channel.txCommit();
                System.out.println("TX End");
             }
        }

    }
}
