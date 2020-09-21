package com.borelanjo.elasticsearchconsumer.application.service;

import com.borelanjo.elasticsearchconsumer.domain.model.Tweet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class TweetServiceTest {
    private TweetService tweetService;
    private String message;

    @BeforeEach
    void setUp() throws IOException {
        generateMessageFromJson("/message.json");

        tweetService = new TweetService(null, new UserService(null));
    }

    @Test
    void shouldCreateTweetByMessage() {

        Tweet tweet = tweetService.extractedFrom(tweetService.getJsonTweet(message));
        assertNotNull(tweet);
        assertNotNull(tweet.getHashtags());
        assertEquals(2, tweet.getHashtags().size());
        assertEquals("dog", tweet.getHashtags().get(0));
        assertEquals("cachorro", tweet.getHashtags().get(1));
        assertNotNull(tweet.getMentions());
        assertEquals(1, tweet.getMentions().size());
        assertEquals("gifsdeanimal", tweet.getMentions().get(0).getScreenName());
        assertEquals("2020-09-20T13:13:23Z", tweet.getCreatedAt());
    }

    @Test
    void shouldCreateTweetByMessageWithTextExtended() throws IOException {
        generateMessageFromJson("/message_text_truncated.json");
        Tweet tweet = tweetService.extractedFrom(tweetService.getJsonTweet(message));
        assertNotNull(tweet);
        assertEquals("Sentei aqui no metr\u00f4 esperando carona e um doguinho me viu tomando \u00e1gua " +
                "e veio perto, ele torou minha garrafa pelo calor tadinho...mas agora t\u00f4 me sentindo dono dele " +
                "pq ele n\u00e3o sai do meu lado, ganhei o domingo j\u00e1", tweet.getText());
    }


    @Test
    void shouldCreateTweetWithUserByMessage() {
        Tweet tweet = tweetService.extractedFrom(tweetService.getJsonTweet(message));
        assertNotNull(tweet);
        assertNull(tweet.getUser().getLanguage());
        assertEquals(1_275_254_537_905_934_336L, tweet.getUser().getId());
        assertEquals("Tue Jun 23 02:29:18 +0000 2020",
                tweet.getUser().getCreatedAt());
        assertEquals("sei n\u00e3o hein \ud83e\udd37\ud83c\udffb\u200d\u2642\ufe0f SW-0009-5620-8913",
                tweet.getUser().getDescription());
        assertEquals("Goi\u00e2nia, Brasil", tweet.getUser().getLocation());
        assertEquals("Gustavo Martinho", tweet.getUser().getName());
        assertEquals("martinhogus", tweet.getUser().getScreenName());
        assertEquals("http://instagram.com/martinhogus", tweet.getUser().getUrl());
        assertEquals("http://pbs.twimg.com/profile_images/1285599992044978176/YeOpuEvC_normal.jpg",
                tweet.getUser().getProfileImageUrl());
        assertEquals(2468, tweet.getUser().getFavouritesCount());
        assertEquals(119, tweet.getUser().getFollowersCount());
        assertEquals(139, tweet.getUser().getFriendsCount());
        assertEquals(0, tweet.getUser().getListedCount());
        assertEquals(605, tweet.getUser().getStatusesCount());
        assertFalse(tweet.getUser().getVerified());
        assertTrue(tweet.getUser().getGeoEnabled());
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private void generateMessageFromJson(String jsonPath) throws IOException {
        var clazz = UserServiceTest.class;
        InputStream inputStream = clazz.getResourceAsStream(jsonPath);
        this.message = readFromInputStream(inputStream);
    }
}