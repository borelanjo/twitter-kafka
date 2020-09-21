package com.borelanjo.elasticsearchconsumer.application.service;

import com.borelanjo.elasticsearchconsumer.domain.model.Tweet;
import com.borelanjo.elasticsearchconsumer.domain.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TweetService extends ElasticSearchService<Tweet> {

    public static final String INDEX = "twitter_tweet";
    public static final String TEXT = "text";
    public static final String FULL_TEXT = "full_text";
    public static final String EXTENDED_TWEET = "extended_tweet";
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String LANGUAGE = "lang";
    public static final String FAVORITED = "favorited";
    public static final String FAVORITE_COUNT = "favorite_count";
    public static final String SOURCE = "source";
    public static final String RETWEETED = "retweeted";
    public static final String RETWEET_COUNT = "retweet_count";
    public static final String REPLY_COUNT = "reply_count";
    public static final String QUOTE_COUNT = "quote_count";
    public static final String TIMESTAMP_MS = "timestamp_ms";
    public static final String USER_MENTIONS = "user_mentions";
    public static final String ENTITIES = "entities";
    public static final String HASHTAGS = "hashtags";

    private final UserService userService;


    public TweetService(RestHighLevelClient client, UserService userService) {
        super(client, INDEX);
        this.userService = userService;
    }

    @Override
    public Tweet extractedFrom(JsonObject jsonTweet) {
        return Tweet
                .builder()
                .id(getAsLong(jsonTweet, ID))
                .text(getText(jsonTweet))
                .user(userService
                        .extractedFrom(userService.getJsonUser(jsonTweet)))
                .mentions(getMentions(jsonTweet))
                .createdAt(getCreatedAt(jsonTweet))
                .language(getAsString(jsonTweet, LANGUAGE))
                .favorited(getAsBoolean(jsonTweet, FAVORITED))
                .favoriteCount(getAsInt(jsonTweet, FAVORITE_COUNT))
                .hashtags(getHashTags(jsonTweet))
                .quoteCount(getAsInt(jsonTweet, QUOTE_COUNT))
                .replyCount(getAsInt(jsonTweet, REPLY_COUNT))
                .retweetCount(getAsInt(jsonTweet, RETWEET_COUNT))
                .retweeted(getAsBoolean(jsonTweet, RETWEETED))
                .source(getAsString(jsonTweet, SOURCE))
                .timestampMs(getAsLong(jsonTweet, TIMESTAMP_MS))
                .build();
    }

    private String getCreatedAt(JsonObject jsonTweet) {
        String[] split = getAsString(jsonTweet, CREATED_AT).split(" ");
        return String.format("%s-%s-%sT%sZ", split[5], getMonth(split[1]), split[2], split[3]);
    }

    private String getMonth(String monthAbr) {
        Optional<Month> optional = Arrays
                .stream(Month.values())
                .filter(m -> m.toString()
                        .contains(monthAbr.toUpperCase()))
                .findFirst();
        return optional
                .map(month -> String.format("%02d", month.getValue()))
                .orElse(null);
    }

    private List<User> getMentions(JsonObject jsonTweet) {
        List<User> mentions = new ArrayList<>();

        jsonTweet
                .get(ENTITIES)
                .getAsJsonObject()
                .get(USER_MENTIONS)
                .getAsJsonArray().
                forEach(jsonElement -> mentions.add(userService.extractedFrom(jsonElement.getAsJsonObject())));

        return mentions;
    }

    private List<String> getHashTags(JsonObject jsonTweet) {
        List<String> hashTags = new ArrayList<>();

        jsonTweet
                .get(ENTITIES)
                .getAsJsonObject()
                .get(HASHTAGS)
                .getAsJsonArray()
                .forEach(jsonElement -> hashTags.add(getAsString(jsonElement.getAsJsonObject(), TEXT)));

        return hashTags;
    }

    private String getText(JsonObject jsonTweet) {
        boolean isTruncated = getAsBoolean(jsonTweet, "truncated");

        return isTruncated ? getAsString(
                jsonTweet
                        .get(EXTENDED_TWEET)
                        .getAsJsonObject(), FULL_TEXT)
                : getAsString(jsonTweet, TEXT);
    }

    public JsonObject getJsonTweet(String message) {
        return JsonParser
                .parseString(message)
                .getAsJsonObject();
    }
}
