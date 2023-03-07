package com.jarkz.quiz.repo;

import com.jarkz.quiz.model.QuizWalkthrough;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface QuizWalkthroughRepo extends JpaRepository<QuizWalkthrough, Long> {
    @Query("SELECT qw FROM QuizWalkthrough AS qw LEFT JOIN FETCH qw.questions as q WHERE qw.nickname = :nickname")
    QuizWalkthrough getQuizWithQuestionsByNickname(@Param(value = "nickname") String nickname);

    @EntityGraph(attributePaths = {"quiz", "questions"})
    @Query("SELECT qw FROM QuizWalkthrough AS qw WHERE qw.nickname = :nickname")
    QuizWalkthrough getQuizWalkthroughWithQuestionsAndRealQuizByNickname(@Param(value = "nickname") String nickname);

    @Query("SELECT qw FROM QuizWalkthrough AS qw LEFT JOIN FETCH qw.questions AS q WHERE qw.nickname = :nickname")
    QuizWalkthrough getQuizWalkthroughWithQuestionsByNickname(@Param(value = "nickname") String nickname);

    QuizWalkthrough findQuizWalkthroughByNickname(String nickname);
}