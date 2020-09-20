package com.borelanjo.elasticsearchconsumer.application.service;

import com.borelanjo.elasticsearchconsumer.domain.model.User;
import com.google.gson.JsonObject;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ElasticSearchService<User> {

    public static final String INDEX = "twitter_user";
    public static final String ID = "id";
    public static final String SCREEN_NAME = "screen_name";
    public static final String FOLLOWERS_COUNT = "followers_count";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_AT = "created_at";
    public static final String LOCATION = "location";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String FAVOURITES_COUNT = "favourites_count";
    public static final String FRIENDS_COUNT = "friends_count";
    public static final String LISTED_COUNT = "listed_count";
    public static final String STATUSES_COUNT = "statuses_count";
    public static final String GEO_ENABLED = "geo_enabled";
    public static final String VERIFIED = "verified";
    public static final String USER = "user";
    public static final String PROFILE_IMAGE_URL = "profile_image_url";
    public static final String LANGUAGE = "language";

    public UserService(RestHighLevelClient client) {
        super(client, INDEX);
    }

    @Override
    public User extractedFrom(JsonObject jsonUser) {
        return User
                .builder()
                .id(getAsLong(jsonUser, ID))
                .screenName(getAsString(jsonUser, SCREEN_NAME))
                .followersCount(getAsInt(jsonUser, FOLLOWERS_COUNT))
                .description(getAsString(jsonUser, DESCRIPTION))
                .createdAt(getAsString(jsonUser, CREATED_AT))
                .location(getAsString(jsonUser, LOCATION))
                .language(getAsString(jsonUser, LANGUAGE))
                .name(getAsString(jsonUser, NAME))
                .url(getAsString(jsonUser, URL))
                .profileImageUrl(getAsString(jsonUser, PROFILE_IMAGE_URL))
                .favouritesCount(getAsInt(jsonUser, FAVOURITES_COUNT))
                .friendsCount(getAsInt(jsonUser, FRIENDS_COUNT))
                .listedCount(getAsInt(jsonUser, LISTED_COUNT))
                .statusesCount(getAsInt(jsonUser, STATUSES_COUNT))
                .geoEnabled(getAsBoolean(jsonUser, GEO_ENABLED))
                .verified(getAsBoolean(jsonUser, VERIFIED))
                .build();
    }

    public JsonObject getJsonUser(JsonObject json) {
        return json
                .get(USER)
                .getAsJsonObject();
    }
}
