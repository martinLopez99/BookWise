package com.dacs.bookwise.service.impl;

import com.dacs.bookwise.dto.QuestionDTO;
import com.dacs.bookwise.entities.Question;
import com.dacs.bookwise.repository.QuestionRepository;
import com.dacs.bookwise.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setWeight(question.getWeight());
        return dto;
    }
}
