package com.froyo.utils;

import com.froyo.common.Queues;

import java.io.*;

public class QueueInfo {

    public static void main(String[] args) {

        CommandRunner runner = new CommandRunner();
        runner.run("rabbitmqadmin get queue=" + Queues.EXCHANGE_QUEUE_NAME);
    }
}
