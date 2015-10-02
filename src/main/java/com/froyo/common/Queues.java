package com.froyo.common;

/**
 * Queues can be declared to be exclusive/temporary, set exclusive=true to have the queue deleted on DISCONNECTION.
 */
public class Queues {

    public final static String QUEUE_NAME = "testqueue";
    public final static String EXCHANGE_QUEUE_NAME = "exchangetestqueue";
}
