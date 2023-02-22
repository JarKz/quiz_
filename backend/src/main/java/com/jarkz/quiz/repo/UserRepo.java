package com.jarkz.quiz.repo;

import com.jarkz.quiz.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByNickname(String nickname);

    @Query("SELECT DISTINCT u FROM QuizUser AS u LEFT JOIN FETCH u.quizzes AS quizzes WHERE u.nickname = :nickname")
    User getUserWithQuizzesByNickname(@Param(value = "nickname") String nickname);
}
