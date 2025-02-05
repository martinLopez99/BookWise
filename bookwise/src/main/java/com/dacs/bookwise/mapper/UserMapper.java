package com.dacs.bookwise.mapper;

import com.dacs.bookwise.dto.*;
import com.dacs.bookwise.entities.BookPreferens;
import com.dacs.bookwise.entities.ReadingEntry;
import com.dacs.bookwise.entities.SurveyResponse;
import com.dacs.bookwise.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Mapping(target = "readingHistory", source = "readingHistory")
    @Mapping(target = "surveyResponses", source = "surveyResponses")
    @Mapping(target = "preferences", source = "preferences")
    UserDTO userToUserDTO(User user);

    default BookPreferensDTO mapPreferences(BookPreferens preferences) {
        if (preferences == null) return null;
        BookPreferensDTO dto = new BookPreferensDTO();

        if (preferences.getGender() != null) {
            dto.setGender(preferences.getGender());
        }

        if (preferences.getTopics() != null) {
            dto.setTopics(preferences.getTopics());
        }
        return dto;
    }

    default List<ReadingEntryDTO> mapReadingHistory(List<ReadingEntry> readingHistory) {
        if (readingHistory == null) return null;
        return readingHistory.stream()
                .map(entry -> new ReadingEntryDTO(entry.getGoogleBookId(), entry.getRating(), entry.getNotes()))
                .toList();
    }

    default List<SurveyResponseDTO> mapSurveyResponses(List<SurveyResponse> surveyResponses) {
        if (surveyResponses == null) return null;
        return surveyResponses.stream()
                .map(response -> {
                    SurveyResponseDTO dto = new SurveyResponseDTO();
                    dto.setSurveyId(response.getSurveyId());

                    if (response.getResponses() != null) {
                        List<AnswerDTO> answers = response.getResponses().stream()
                                .map(answer -> {
                                    AnswerDTO answerDTO = new AnswerDTO();
                                    answerDTO.setQuestionId(answer.getQuestionId());

                                    if (answer.getSelectedOption() != null) {
                                        OptionDTO optionDTO = new OptionDTO();
                                        optionDTO.setAnswer(answer.getSelectedOption().getAnswer());
                                        optionDTO.setWeight(answer.getSelectedOption().getWeight());
                                        answerDTO.setSelectedOption(optionDTO);
                                    }

                                    return answerDTO;
                                })
                                .toList();
                        dto.setResponses(answers);
                    }
                    return dto;
                })
                .toList();
    }

}
