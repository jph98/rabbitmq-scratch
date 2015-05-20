package com.froyo;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;

public class SimplePublisher {

    public SimplePublisher() {

    }

    public static void main(String[] args) {

        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(Queues.QUEUE_NAME, false, false, false, null);

            String message = "Bongo " + UUID.randomUUID();
            channel.basicPublish("", Queues.QUEUE_NAME, null, message.getBytes());
            System.out.println("\t[x] Sent '" + message + "'");

        } catch (IOException e) {
            System.out.println("IOException " + e);
        }


    }
}
