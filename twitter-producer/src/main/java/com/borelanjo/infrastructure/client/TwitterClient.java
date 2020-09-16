package com.borelanjo.infrastructure.client;

import com.borelanjo.infrastructure.factory.HosebirdAuthFactory;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TwitterClient {

    private static final String CLIENT_NAME = "Borelanjo Kafka App";

    public Client create(BlockingQueue<String> msgQueue, List<String> terms) {

        return new ClientBuilder()
                .name(CLIENT_NAME)
                .hosts(new HttpHosts(Constants.STREAM_HOST))
                .authentication(new HosebirdAuthFactory().create())
                .endpoint(new StatusesFilterEndpoint().trackTerms(terms))
                .processor(new StringDelimitedProcessor(msgQueue)
                ).build();
    }
}
