package com.borelanjo.application.configuration.property;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class TwitterProperty {
    private final String TWITTER_CLIENT_CONSUMER = "twitter.client.consumer.key";
    private final String TWITTER_CLIENT_CONSUMER_SECRET = "twitter.client.consumer.secret";
    private final String TWITTER_TOKEN = "twitter.client.token";
    private final String TWITTER_TOKEN_SECRET = "twitter.client.token-secret";

    private final String PROPERTY_FILENAME = "application.properties";


    private Properties prop;
    private String consumerKey;
    private String consumerSecret;
    private String token;
    private String tokenSecret;

    public TwitterProperty() {
        prop = new PropertiesReader().read(PROPERTY_FILENAME);
        this.consumerKey = getProperty(TWITTER_CLIENT_CONSUMER);
        this.consumerSecret = getProperty(TWITTER_CLIENT_CONSUMER_SECRET);
        this.token = getProperty(TWITTER_TOKEN);
        this.tokenSecret = getProperty(TWITTER_TOKEN_SECRET);

    }

    public String getProperty(String key) {
        return prop.getProperty(key);
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }
}
