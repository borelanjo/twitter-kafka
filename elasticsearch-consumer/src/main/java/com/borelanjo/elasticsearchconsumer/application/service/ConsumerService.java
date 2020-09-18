package com.borelanjo.elasticsearchconsumer.application.service;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConsumerService {

    private final TweetService tweetService;
    private final RestHighLevelClient client;

    public ConsumerService(TweetService tweetService, RestHighLevelClient client) {
        this.tweetService = tweetService;
        this.client = client;
    }

    @KafkaListener(topics = "twitter_tweets", groupId = "tweets")
    public void consumer(String message) {
        IndexRequest indexRequest = new IndexRequest("tweet");
        indexRequest.source(message, XContentType.JSON);

        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Received Message in group tweets: " + message);
    }
}
