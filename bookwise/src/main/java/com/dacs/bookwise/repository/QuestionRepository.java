package com.dacs.bookwise.repository;

import com.dacs.bookwise.entities.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
}
