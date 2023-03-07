package com.jarkz.quiz.types.model;

import java.util.List;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class QuestionResult {
    private String content;
    private List<String> answers;
    private String currentAnswer;
    private List<String> rightAnswers;
    private String type;
    private String walkthrough;
}