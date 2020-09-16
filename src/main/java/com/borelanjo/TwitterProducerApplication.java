package com.borelanjo;

import com.borelanjo.infrastructure.client.TwitterClient;
import com.borelanjo.infrastructure.factory.KafkaProducerFactory;
import com.google.common.collect.Lists;
import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducerApplication {

    private Logger logger = LoggerFactory.getLogger("TwitterProducerApplication");

    private List<String> terms = Lists.newArrayList("covid");

    public static void main(String[] args) {
        new TwitterProducerApplication().run();
    }

    public void run() {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1_000);


        Client client = new TwitterClient().create(msgQueue, terms);

        client.connect();

        KafkaProducer<String, String> producer = new KafkaProducerFactory().create();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping application");

            logger.info("shutting down client from twitter...");
            client.stop();

            logger.info("closing producer");
            producer.close();

            logger.info("bye bye!");
        }));

        try {
            while (!client.isDone()) {
                String msg = msgQueue.poll(5, TimeUnit.SECONDS);
                if (msg != null) {
                    producer.send(new ProducerRecord<>("twitter_tweets", null, msg),
                            (recordMetadata, e) -> {
                                if (e != null) {
                                    logger.error("Something bad happened", e);
                                }
                            });
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            client.stop();
            producer.close();
            Thread.currentThread().interrupt();
        } finally {
            client.stop();
            producer.close();
        }

        logger.info("The end");

    }

}
