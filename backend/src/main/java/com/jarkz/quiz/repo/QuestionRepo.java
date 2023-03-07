package com.jarkz.quiz.repo;

import com.jarkz.quiz.model.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    Question findQuestionById(long questionId);

    List<Question> findQuestionByIdIn(List<Long> questionId);
}
