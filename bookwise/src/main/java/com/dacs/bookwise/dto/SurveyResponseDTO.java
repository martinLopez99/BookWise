package com.dacs.bookwise.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class SurveyResponseDTO {
    @JsonProperty("surveyId")
    private String surveyId;
    @JsonProperty("responses")
    private List<AnswerDTO> responses;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public List<AnswerDTO> getResponses() {
        return responses;
    }

    public void setResponses(List<AnswerDTO> responses) {
        this.responses = responses;
    }

}
