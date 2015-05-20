package com.froyo;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class QueueLister {

    public static void main(String[] args) {

        try {
            Process proc = Runtime.getRuntime().exec("rabbitmqadmin get queue=" + Queues.QUEUE_NAME + " requeue=false");

            InputStream inputStream = proc.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println("Could not list messages" + e);
        }
    }
}
