package com.dacs.bookwise.dto;

import java.util.List;


public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    //private BookPreferensDTO preferences;
    private String preferences;
    private List<String> wishlistBookIds;
    private List<ReadingEntryDTO> readingHistory;
    private List<SurveyResponseDTO> surveyResponses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public List<String> getWishlistBookIds() {
        return wishlistBookIds;
    }

    public void setWishlistBookIds(List<String> wishlistBookIds) {
        this.wishlistBookIds = wishlistBookIds;
    }

    public List<ReadingEntryDTO> getReadingHistory() {
        return readingHistory;
    }

    public void setReadingHistory(List<ReadingEntryDTO> readingHistory) {
        this.readingHistory = readingHistory;
    }

    public List<SurveyResponseDTO> getSurveyResponses() {
        return surveyResponses;
    }

    public void setSurveyResponses(List<SurveyResponseDTO> surveyResponses) {
        this.surveyResponses = surveyResponses;
    }

}