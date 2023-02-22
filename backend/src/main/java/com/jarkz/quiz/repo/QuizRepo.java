package com.jarkz.quiz.repo;

import com.jarkz.quiz.model.Quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface QuizRepo extends JpaRepository<Quiz, Long> {

    Quiz findQuizById(long id);

    @EntityGraph(attributePaths = {"user", "questions"})
    @Query("SELECT q FROM Quiz AS q WHERE EXISTS(SELECT question FROM Question AS question WHERE question.quiz = q)")
    Page<Quiz> findAllByQuizWhereQuestionNotEmpty(Pageable pageable);
    
    @Query("SELECT DISTINCT q FROM Quiz AS q LEFT JOIN FETCH q.questions AS questions WHERE q.id = :id")
    Quiz getQuizWithQuestionsById(@Param(value = "id") long id);
    
    @Query("SELECT DISTINCT q FROM Quiz AS q LEFT JOIN FETCH q.user AS user WHERE q.id = :id")
    Quiz getQuizWithUserById(@Param(value = "id") long id);
}
