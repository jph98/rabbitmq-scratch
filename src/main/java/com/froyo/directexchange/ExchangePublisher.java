package com.froyo.directexchange;

import com.froyo.common.ExchangeType;
import com.froyo.common.Exchanges;
import com.froyo.common.Queues;
import com.froyo.common.RoutingKeys;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Note that this uses the default exchange
 */
public class ExchangePublisher {

    public ExchangePublisher() {

    }

    public static void main(String[] args) {

        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();


            // 1. Declare the exchange (direct)
            // fanout exchanges ignroe the routing key

            channel.exchangeDeclare(Exchanges.SIMPLE_EXCHANGE, ExchangeType.DIRECT, true);

            // 2. Declare the queue
            channel.queueDeclare(Queues.EXCHANGE_QUEUE_NAME, false, false, false, null);

            // 3. Bind our queue to the exchange
            channel.queueBind(Queues.EXCHANGE_QUEUE_NAME, Exchanges.SIMPLE_EXCHANGE, RoutingKeys.SIMPLE_ROUTING_KEY);

            long start = System.currentTimeMillis();
            int sent = 0;
            for (int n = 0; n < 10; n++) {


                String message = buildMessage();

                // Use a routing key to specify where this is going
                channel.basicPublish(Exchanges.SIMPLE_EXCHANGE, RoutingKeys.SIMPLE_ROUTING_KEY, null, message.getBytes());

                System.out.println("\t[x] Sent: " + message + " to: " + Exchanges.SIMPLE_EXCHANGE + " with routing key: " + RoutingKeys.SIMPLE_ROUTING_KEY);
                sent++;
            }

            System.out.println("Sent " + sent + " messages");

            long taken = System.currentTimeMillis() - start;
            System.out.println("Send time: " + taken / 1000 + " secs OR " + taken + " ms");

            System.out.println("Running...");

        } catch (IOException e) {
            System.out.println("IOException " + e);
        }


    }

    /**
     * We use a simple message here and just grab the bytes from it
     * We can also use a serialisation mechanism (JSON, BSON whatever)
     * Messages have metadata attached to them (persistent, mandatory, timestamp)
     * Messages also have a HEADERS table (could be keys for anything)
     *
     * @return
     */
    private static String buildMessage() {
        return "ExchangeMessage-" + UUID.randomUUID();
    }
}
