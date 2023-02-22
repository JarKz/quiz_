package com.jarkz.quiz.model;

import com.jarkz.quiz.MainApplication;
import com.jarkz.quiz.configurations.TestDatabaseConfiguration;
import com.jarkz.quiz.model.generator.EntitiesGenerator;
import com.jarkz.quiz.repo.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        MainApplication.class,
        TestDatabaseConfiguration.class
})
@ActiveProfiles("test")
public class TestEntitiesAndRepos {

    private final UserRepo userRepo;
    private final QuizRepo quizRepo;
    private final QuestionRepo questionRepo;

    private final EntitiesGenerator generator;
    private final String USER_NICKNAME = "balabol";
    private final String CREATOR_NICKNAME = "balabolka";
    private final String ADMIN_NICKNAME = "nebalabol";


    @Autowired
    public TestEntitiesAndRepos(
        UserRepo userRepo,
        QuizRepo quizRepo,
        QuestionRepo questionRepo,
        PasswordEncoder passwordEncoder
    ){
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;

        generator = new EntitiesGenerator(
                USER_NICKNAME,
                CREATOR_NICKNAME,
                ADMIN_NICKNAME,
                passwordEncoder
        );
    }


    @Test
    @Transactional
    public void testEntities(){
        User user = generator.generateUser();
        User creator = generator.generateCreator();
        User admin = generator.generateAdmin();
        userRepo.save(user);
        userRepo.save(creator);
        userRepo.save(admin);
        userRepo.flush();

        User foundUser = userRepo.findUserByNickname(USER_NICKNAME);
        assertThat(foundUser).isEqualTo(user);
        List<Quiz> emptyQuizzes = foundUser.getQuizzes();
        assertThat(emptyQuizzes).isEmpty();

        List<Quiz> allQuizzes = quizRepo.findAll();
        assertThat(allQuizzes.size()).isEqualTo(3);

        User foundCreator = userRepo.findUserByNickname(CREATOR_NICKNAME);
        assertThat(foundCreator).isEqualTo(creator);
        List<Quiz> creatorQuizzes = foundCreator.getQuizzes();
        assertThat(creatorQuizzes).isNotEmpty().isNotNull();
        assertThat(creatorQuizzes.size()).isEqualTo(1);
        List<Question> firstQuizQuestions = creatorQuizzes.get(0).getQuestions();
        assertThat(firstQuizQuestions).isNotEmpty().isNotNull();
        assertThat(firstQuizQuestions.size()).isEqualTo(6);

        User foundAdmin = userRepo.findUserByNickname(ADMIN_NICKNAME);
        assertThat(foundAdmin).isEqualTo(admin);
        List<Quiz> adminQuizzes = foundAdmin.getQuizzes();
        assertThat(adminQuizzes).isNotEmpty().isNotNull();
        assertThat(adminQuizzes.size()).isEqualTo(2);
        List<Question> secondQuizQuestions = adminQuizzes.get(0).getQuestions();
        assertThat(secondQuizQuestions).isNotEmpty().isNotNull();
        assertThat(secondQuizQuestions.size()).isEqualTo(3);
        List<Question> thirdQuizQuestions = adminQuizzes.get(1).getQuestions();
        assertThat(thirdQuizQuestions).isNotEmpty().isNotNull();
        assertThat(thirdQuizQuestions.size()).isEqualTo(4);

        List<Question> allQuestions = questionRepo.findAll();
        assertThat(allQuestions).isNotEmpty().isNotNull();
        assertThat(allQuestions.size()).isEqualTo(13);
    }

    //Don't assert users (or creator, or admin) because even if user have quizzes
    //but these quizzes don't have questions because initialization are lazy
    @Test
    public void TestQueriesWithoutTransactionsForUser(){
        User user = generator.generateUser();
        userRepo.save(user);

        User foundUser = userRepo.getUserWithQuizzesByNickname(USER_NICKNAME);
        assertThat(foundUser.getQuizzes()).isEmpty();

        List<Quiz> allQuizzes = quizRepo.findAll();
        assertThat(allQuizzes.size()).isEqualTo(0);

        userRepo.deleteAll();
        quizRepo.deleteAll();
        questionRepo.deleteAll();
    }
    @Test
    public void TestQueriesWithoutTransactionsForCreator(){
        User creator = generator.generateCreator();
        userRepo.save(creator);
        userRepo.flush();

        User foundCreator = userRepo.getUserWithQuizzesByNickname(CREATOR_NICKNAME);
        List<Quiz> creatorQuizzes = foundCreator.getQuizzes();
        assertThat(creatorQuizzes).isNotEmpty().isNotNull();
        assertThat(creatorQuizzes.size()).isEqualTo(1);
        List<Question> firstQuizQuestions = quizRepo
                .getQuizWithQuestionsById(creatorQuizzes.get(0).getId())
                .getQuestions();
        assertThat(firstQuizQuestions).isNotEmpty().isNotNull();
        assertThat(firstQuizQuestions.size()).isEqualTo(6);

        List<Quiz> allQuizzes = quizRepo.findAll();
        assertThat(allQuizzes.size()).isEqualTo(1);
        userRepo.deleteAll();
        quizRepo.deleteAll();
        questionRepo.deleteAll();
    }
    @Test
    public void TestQueriesWithoutTransactionsForAdmin(){
        User admin = generator.generateAdmin();
        userRepo.save(admin);
        userRepo.flush();

        User foundAdmin = userRepo.getUserWithQuizzesByNickname(ADMIN_NICKNAME);
        List<Quiz> adminQuizzes = foundAdmin.getQuizzes();
        assertThat(adminQuizzes).isNotEmpty().isNotNull();
        assertThat(adminQuizzes.size()).isEqualTo(2);
        List<Question> secondQuizQuestions = quizRepo
                .getQuizWithQuestionsById(adminQuizzes.get(0).getId())
                .getQuestions();
        assertThat(secondQuizQuestions).isNotEmpty().isNotNull();
        assertThat(secondQuizQuestions.size()).isEqualTo(3);
        List<Question> thirdQuizQuestions = quizRepo
                .getQuizWithQuestionsById(adminQuizzes.get(1).getId())
                .getQuestions();
        assertThat(thirdQuizQuestions).isNotEmpty().isNotNull();
        assertThat(thirdQuizQuestions.size()).isEqualTo(4);

        List<Quiz> allQuizzes = quizRepo.findAll();
        assertThat(allQuizzes.size()).isEqualTo(2);
        userRepo.deleteAll();
        quizRepo.deleteAll();
        questionRepo.deleteAll();
    }

    @Test
    public void checkRightSettingForRepos(){
        User user = generator.generateUser();
        User creator = generator.generateCreator();
        User admin = generator.generateAdmin();
        userRepo.save(user);
        userRepo.save(creator);
        userRepo.save(admin);
        userRepo.flush();

        List<Question> allQuestions = questionRepo.findAll();
        assertThat(allQuestions).isNotEmpty().isNotNull();
        assertThat(allQuestions.size()).isEqualTo(13);

        List<Quiz> allQuizzes = quizRepo.findAll();
        assertThat(allQuizzes.size()).isEqualTo(3);
        userRepo.deleteAll();
        quizRepo.deleteAll();
        questionRepo.deleteAll();
    }
}
