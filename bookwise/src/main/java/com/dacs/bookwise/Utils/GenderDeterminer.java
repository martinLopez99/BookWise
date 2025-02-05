package com.dacs.bookwise.Utils;

import com.dacs.bookwise.dto.AnswerDTO;
import com.dacs.bookwise.dto.SurveyResponseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenderDeterminer {

    public static String determinePreferredGenre(List<SurveyResponseDTO> surveyResponses) {
        Map<String, Integer> genreScores = new HashMap<>();

        for (SurveyResponseDTO survey : surveyResponses) {
            for (AnswerDTO answer : survey.getResponses()) {
                String genre = questionGenreMap.get(answer.getQuestionId());
                if (genre != null) {
                    int score = answer.getSelectedOption().getAnswer() * answer.getSelectedOption().getWeight();
                    genreScores.put(genre, genreScores.getOrDefault(genre, 0) + score);
                }
            }
        }


        return genreScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin preferencia");
    }


    private static final Map<String, String> questionGenreMap = Map.ofEntries(
            Map.entry("675f6ae6382d0623cccf7e01", "Fantasía"),
            Map.entry("675f6ae6382d0623cccf7e02", "Drama"),
            Map.entry("675f6ae6382d0623cccf7e03", "Suspenso"),
            Map.entry("675f6ae6382d0623cccf7e04", "Ciencia Ficción"),
            Map.entry("675f6ae6382d0623cccf7e05", "Aventura"),
            Map.entry("675f6ae6382d0623cccf7e06", "Ética"),
            Map.entry("675f6ae6382d0623cccf7e07", "Fantasía"),
            Map.entry("675f6ae6382d0623cccf7e08", "Fantasía"),
            Map.entry("675f6ae6382d0623cccf7e09", "Suspenso"),
            Map.entry("675f6ae6382d0623cccf7e10", "Misterio"),
            Map.entry("675f6ae6382d0623cccf7e11", "Misterio"),
            Map.entry("675f6ae6382d0623cccf7e12", "Misterio"),
            Map.entry("675f6ae6382d0623cccf7e13", "Romance"),
            Map.entry("675f6ae6382d0623cccf7e14", "Romance"),
            Map.entry("675f6ae6382d0623cccf7e15", "Romance"),
            Map.entry("675f6ae6382d0623cccf7e16", "Histórico"),
            Map.entry("675f6ae6382d0623cccf7e17", "Histórico"),
            Map.entry("675f6ae6382d0623cccf7e18", "Histórico"),
            Map.entry("675f6ae6382d0623cccf7e19", "Terror"),
            Map.entry("675f6ae6382d0623cccf7e20", "Terror")
    );
}
