package com.dacs.bookwise.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "User")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    //private BookPreferens preferences = new BookPreferens();

    private String preferences; //SOLO NECESITO DEVOLVER EL GENERO PREFERIDO

    private List<String> wishlistBookIds = new ArrayList<>();
    @Field("readingHistory")
    private List<ReadingEntry> readingHistory = new ArrayList<>();
    private List<SurveyResponse> surveyResponses = new ArrayList<>();

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

    public List<ReadingEntry> getReadingHistory() {
        return readingHistory;
    }

    public void setReadingHistory(List<ReadingEntry> readingHistory) {
        this.readingHistory = readingHistory;
    }

    public List<SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    public void setSurveyResponses(List<SurveyResponse> surveyResponses) {
        this.surveyResponses = surveyResponses;
    }
}

