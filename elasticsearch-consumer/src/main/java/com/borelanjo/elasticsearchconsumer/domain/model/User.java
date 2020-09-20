package com.borelanjo.elasticsearchconsumer.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Builder
@Data
public class User {

    private Long id;
    private String name;
    private String screenName;
    private String location;
    private String language;
    private String url;
    private String profileImageUrl;
    private String description;
    private Integer followersCount;
    private Integer friendsCount;
    private Integer listedCount;
    private Integer favouritesCount;
    private Integer statusesCount;
    private String createdAt;
    private Boolean verified;
    private Boolean geoEnabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", screenName='" + screenName + '\'' +
                '}';
    }
}
