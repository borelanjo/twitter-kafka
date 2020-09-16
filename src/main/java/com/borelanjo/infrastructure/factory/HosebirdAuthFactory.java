package com.borelanjo.infrastructure.factory;

import com.borelanjo.application.configuration.property.TwitterProperty;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class HosebirdAuthFactory {

    private TwitterProperty twitterProperty;

    public HosebirdAuthFactory() {
        twitterProperty = new TwitterProperty();
    }

    public Authentication create() {
        return new OAuth1(
                twitterProperty.getConsumerKey(),
                twitterProperty.getConsumerSecret(),
                twitterProperty.getToken(),
                twitterProperty.getTokenSecret());
    }
}
