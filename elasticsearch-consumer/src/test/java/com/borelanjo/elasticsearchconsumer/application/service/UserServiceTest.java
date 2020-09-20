package com.borelanjo.elasticsearchconsumer.application.service;

import com.borelanjo.elasticsearchconsumer.domain.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private String message;

    @BeforeEach
    void setUp() throws IOException {
        var clazz = UserServiceTest.class;
        InputStream inputStream = clazz.getResourceAsStream("/message.json");
        this.message = readFromInputStream(inputStream);

        userService = new UserService(null);
    }

    @Test
    void shouldCreateUserByMessage() {
        JsonObject jsonUser = userService.getJsonUser(JsonParser
                .parseString(message)
                .getAsJsonObject());

        User user = userService.extractedFrom(jsonUser);
        assertNotNull(user);
        assertNull(user.getLanguage());
        assertEquals(1_275_254_537_905_934_336L, user.getId());
        assertEquals("Tue Jun 23 02:29:18 +0000 2020",
                user.getCreatedAt());
        assertEquals("sei n\u00e3o hein \ud83e\udd37\ud83c\udffb\u200d\u2642\ufe0f SW-0009-5620-8913", user.getDescription());
        assertEquals("Goi\u00e2nia, Brasil", user.getLocation());
        assertEquals("Gustavo Martinho", user.getName());
        assertEquals("martinhogus", user.getScreenName());
        assertEquals("http://instagram.com/martinhogus", user.getUrl());
        assertEquals("http://pbs.twimg.com/profile_images/1285599992044978176/YeOpuEvC_normal.jpg", user.getProfileImageUrl());
        assertEquals(2468, user.getFavouritesCount());
        assertEquals(119, user.getFollowersCount());
        assertEquals(139, user.getFriendsCount());
        assertEquals(0, user.getListedCount());
        assertEquals(605, user.getStatusesCount());
        assertFalse(user.getVerified());
        assertTrue(user.getGeoEnabled());
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

}