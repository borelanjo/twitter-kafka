package com.borelanjo.elasticsearchconsumer.application.service;

import com.borelanjo.elasticsearchconsumer.domain.model.Tweet;
import com.borelanjo.elasticsearchconsumer.domain.model.User;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    private final UserService userService;
    private final TweetService tweetService;

    public ConsumerService(UserService userService, TweetService tweetService) {
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @KafkaListener(topics = "twitter_tweets", groupId = "tweets")
    public void consumer(String message) {
        JsonObject jsonTweet = tweetService.getJsonTweet(message);
        Tweet tweet = tweetService.extractedFrom(jsonTweet);
        tweetService.create(tweet, tweet.getId().toString());

        JsonObject jsonUser = userService.getJsonUser(jsonTweet);
        User user = userService.extractedFrom(jsonUser);
        userService.create(user, user.getId().toString());

        log.info("Received Message in group tweets: " + message);
    }
}
