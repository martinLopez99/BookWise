package com.dacs.bookwise.dto;

public class AnswerDTO {
    private String questionId;
    private OptionDTO selectedOption;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public OptionDTO getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(OptionDTO selectedOption) {
        this.selectedOption = selectedOption;
    }
}
