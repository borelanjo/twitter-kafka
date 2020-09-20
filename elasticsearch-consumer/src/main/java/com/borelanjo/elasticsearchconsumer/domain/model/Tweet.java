package com.borelanjo.elasticsearchconsumer.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Builder
@Data
public class Tweet {

    private Long id;
    private String text;
    private String source;
    private List<String> hashtags;
    private User user;
    private List<User> mentions;
    private String language;
    private Boolean favorited;
    private Boolean retweeted;
    private Integer quoteCount;
    private Integer replyCount;
    private Integer retweetCount;
    private Integer favoriteCount;
    private Long timestampMs;
    private String createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return id.equals(tweet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "text='" + text + '\'' +
                ", user=" + user +
                '}';
    }
}
