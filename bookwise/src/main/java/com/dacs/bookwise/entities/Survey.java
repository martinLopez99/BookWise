package com.dacs.bookwise.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Survey")
public class Survey {
    @Id
    private String id;
    private String title;
    private String description;
    private List<Question> questions;
}
