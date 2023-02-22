package com.jarkz.quiz.repo;

import com.jarkz.quiz.model.QuizWalkthrough;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizWalkthroughRepo extends JpaRepository<QuizWalkthrough, Long> {
    QuizWalkthrough findQuizWalkthroughByNickname(String nickname);
}