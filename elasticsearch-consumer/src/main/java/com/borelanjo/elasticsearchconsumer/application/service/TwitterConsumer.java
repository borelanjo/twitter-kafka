package com.borelanjo.elasticsearchconsumer.application.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TwitterConsumer {

    @KafkaListener(topics = "twitter_tweets", groupId = "tweets")
    public void consumer(String message) {
        System.out.println("Received Message in group foo: " + message);
    }
}
