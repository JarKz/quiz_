package com.jarkz.quiz.repo;

import com.jarkz.quiz.model.QuestionWalkthrough;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface QuestionWalkthroughRepo extends JpaRepository<QuestionWalkthrough, Long> {
    @Query("SELECT qw FROM QuestionWalkthrough AS qw LEFT JOIN FETCH qw.question AS q WHERE qw.id = :id")
    Optional<QuestionWalkthrough> getQuestionWalkthroughWithRealQuestionById(@Param(value = "id") long id);

    @Query("SELECT qw FROM QuestionWalkthrough AS qw LEFT JOIN FETCH qw.question AS q WHERE qw.id IN :ids")
    List<QuestionWalkthrough> getQuestionWalkthroughWithRealQuestionsByIds(@Param(value = "ids") List<Long> ids);
}
