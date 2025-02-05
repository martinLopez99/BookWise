package com.dacs.bookwise.entities;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class BookPreferens {
    @Field("gender")
    private List<String> gender;
    @Field("topics")
    private List<String> topics;

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}