package com.dacs.bookwise.entities;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


public class SurveyResponse {
    @Field("surveyId")
    private String surveyId;
    @Field("responses")
    private List<Answer> responses;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public List<Answer> getResponses() {
        return responses;
    }

    public void setResponses(List<Answer> responses) {
        this.responses = responses;
    }
}
