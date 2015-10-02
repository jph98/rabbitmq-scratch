package com.froyo.common;

/**
 * Can also have CUSTOM exchange types.
 */
public class ExchangeType {

    // Sent to only one queue/binding specified by a ROUTING KEY
    public static final String DIRECT = "direct";

    // Sent to ALL queue/bindings we know about
    // https://github.com/ruby-amqp/amqp/raw/master/docs/diagrams/004_fanout_exchange.png
    public static final String FANOUT = "fanout";

    // Topic exchange used for publish/subscribe type scenarios
    public static final String TOPIC = "topic";

    // We can do headers based routing (these ignore the routing key).  Must declare a headers exchange.  Useful for multi-step workflows.
    public static final String HEADERS = "headers";
}
