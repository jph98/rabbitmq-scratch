package com.froyo.directexchange;

import com.froyo.common.Ack;
import com.froyo.common.Exchanges;
import com.froyo.common.Queues;
import com.froyo.common.RoutingKeys;
import com.rabbitmq.client.*;

import java.io.IOException;

public class DefaultConsumerProcess {

    public DefaultConsumerProcess() {

    }

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // Get the queue
        channel.queueDeclare(Queues.EXCHANGE_QUEUE_NAME, false, false, false, null);

        System.out.println(" [*] Waiting for messages on " + Queues.EXCHANGE_QUEUE_NAME+ " to exit press CTRL+C");

        channel.queueBind(Queues.EXCHANGE_QUEUE_NAME, Exchanges.SIMPLE_EXCHANGE, RoutingKeys.SIMPLE_ROUTING_KEY);

        // Override the default consumer channel to handle the message
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received routing key: '" + envelope.getRoutingKey() + "' and message:'" + message + "'");

                System.out.println("\nWaiting ...");

                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                }

                // Ack a single message only
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(Queues.EXCHANGE_QUEUE_NAME, Ack.NO_AUTO_ACK, consumer);
    }
}
