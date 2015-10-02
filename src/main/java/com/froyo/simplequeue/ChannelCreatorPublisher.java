package com.froyo.simplequeue;

import com.froyo.common.Queues;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;

/**
 * Note that this uses the default exchange
 */
public class ChannelCreatorPublisher {

    private static final String EXCHANGE = "";

    public ChannelCreatorPublisher() {

    }

    public static void main(String[] args) {

        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();

            channel.queueDeclare(Queues.QUEUE_NAME, false, false, false, null);

            long start = System.currentTimeMillis();
            int sent = 0;
            for (int n = 0; n < 10; n++) {
                String message = "Bongo " + UUID.randomUUID();

                // Use the default exchange
                channel.basicPublish(EXCHANGE, Queues.QUEUE_NAME, null, message.getBytes());

                System.out.println("\t[x] Sent: " + message + " ");
                sent++;
            }

            System.out.println("Sent " + sent + " messages to " + EXCHANGE);

            long taken = System.currentTimeMillis() - start;
            System.out.println("Send time: " + taken / 1000 + " secs OR " + taken + " ms");

            System.out.println("Running...");

        } catch (IOException e) {
            System.out.println("IOException " + e);
        }


    }
}
