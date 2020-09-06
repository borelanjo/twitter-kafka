package com.borelanjo;

import com.borelanjo.infrastructure.client.TwitterClient;
import com.borelanjo.infrastructure.producer.KafkaProducerCreator;
import com.google.common.collect.Lists;
import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducerApplication {
    
    Logger logger = LoggerFactory.getLogger("TwitterProducerApplication");
    
    public static void main(String[] args) {
        new TwitterProducerApplication().run();
    }

    public void run() {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1_000);
        List<String> terms = Lists.newArrayList("Deus", "Igreja Batista", "Adorar");

        Client client = new TwitterClient().create(msgQueue, terms);

        client.connect();

        KafkaProducer<String, String> producer = new KafkaProducerCreator().create();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping application");

            logger.info("shutting down client from twitter...");
            client.stop();

            logger.info("closing producer");
            producer.close();

            logger.info("bye bye!");
        }));

        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }

            if(msg != null){
                producer.send(new ProducerRecord<>("twitter_tweets", null, msg), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if(e != null){
                            logger.error("Something bad happened", e);
                        }
                    }
                });
            }
        }

        logger.info("The end");
        client.stop();
    }

}
