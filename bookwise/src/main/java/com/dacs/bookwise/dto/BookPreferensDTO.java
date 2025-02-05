package com.dacs.bookwise.dto;

import java.util.List;


public class BookPreferensDTO {
    private List<String> gender;
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

