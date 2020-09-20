package com.borelanjo.elasticsearchconsumer.application.service;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

import static java.util.Objects.isNull;

public abstract class ElasticSearchService<T> {

    private final RestHighLevelClient client;
    private final String index;

    private final Gson gson = new Gson();

    public ElasticSearchService(RestHighLevelClient client, String index) {
        this.client = client;
        this.index = index;
    }

    public void create(T json, String id) {
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(gson.toJson(json), XContentType.JSON);
        indexRequest.id(id);

        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public abstract T extractedFrom(JsonObject jsonObject);

    protected Boolean getAsBoolean(JsonObject jsonObject, String memberName) {
        if (isNull(jsonObject.get(memberName)) || jsonObject.get(memberName) instanceof JsonNull) {
            return null;
        }
        return jsonObject.get(memberName).getAsBoolean();
    }

    protected Long getAsLong(JsonObject jsonObject, String memberName) {
        if (isNull(jsonObject.get(memberName)) || jsonObject.get(memberName) instanceof JsonNull) {
            return null;
        }
        return jsonObject.get(memberName).getAsLong();
    }

    protected Integer getAsInt(JsonObject jsonObject, String memberName) {
        if (isNull(jsonObject.get(memberName)) || jsonObject.get(memberName) instanceof JsonNull) {
            return null;
        }
        return jsonObject.get(memberName).getAsInt();
    }

    protected String getAsString(JsonObject jsonObject, String memberName) {
        if (isNull(jsonObject.get(memberName)) || jsonObject.get(memberName) instanceof JsonNull) {
            return null;
        }
        return jsonObject.get(memberName).getAsString();
    }

}
