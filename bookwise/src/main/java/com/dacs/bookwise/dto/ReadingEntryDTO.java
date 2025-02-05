package com.dacs.bookwise.dto;

public class ReadingEntryDTO {
    private String googleBookId;
    private Integer rating;
    private String notes;

    public ReadingEntryDTO() {
    }

    public ReadingEntryDTO(String googleBookId, Integer rating, String notes) {
        this.googleBookId = googleBookId;
        this.rating = rating;
        this.notes = notes;
    }

    public String getGoogleBookId() {
        return googleBookId;
    }

    public void setGoogleBookId(String googleBookId) {
        this.googleBookId = googleBookId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}