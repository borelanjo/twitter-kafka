package com.borelanjo;

import com.borelanjo.application.configuration.property.TwitterProperty;
import com.borelanjo.infrastructure.client.TwitterClient;
import com.twitter.hbc.core.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducerApplication {
    
    Logger logger = LoggerFactory.getLogger("TwitterProducerApplication");
    
    public static void main(String[] args) {
        new TwitterProducerApplication().run();
    }

    public void run() {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100_000);

        Client client = new TwitterClient().create(msgQueue);

        client.connect();

        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }

            if(msg != null){
                logger.info(msg);
            }
        }
        
        client.stop();
    }

}
