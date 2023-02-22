package com.jarkz.quiz.model.generator;

import com.jarkz.quiz.model.*;
import com.jarkz.quiz.types.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class EntitiesGenerator {


    private final String userNickname;
    private final String creatorNickname;
    private final String adminNickname;
    private final PasswordEncoder encoder;
    
    public EntitiesGenerator(
            String userNickname,
            String creatorNickname,
            String adminNickname
    ){
        this.userNickname = userNickname;
        this.creatorNickname = creatorNickname;
        this.adminNickname = adminNickname;
        encoder = new BCryptPasswordEncoder();
    }

    public EntitiesGenerator(
            String userNickname,
            String creatorNickname,
            String adminNickname,
            PasswordEncoder encoder
    ){
        this.userNickname = userNickname;
        this.creatorNickname = creatorNickname;
        this.adminNickname = adminNickname;
        this.encoder = encoder;
    }


    public User generateUser(){
        String firstUserName = "Harry";
        String firstUserPassword = "fsaffhba";
        Role firstUserRole = Role.USER;
        return generateUserWith(userNickname, firstUserName, firstUserPassword, firstUserRole);
    }

    public User generateCreator(){
        String secondUserName = "Helen";
        String secondUserPassword = "fjafhba";
        Role secondUserRole = Role.CREATOR;
        User creator = generateUserWith(creatorNickname, secondUserName, secondUserPassword, secondUserRole);
        Quiz firstQuiz = new Quiz();
        creator.addQuiz(
                generateQuiz(firstQuiz, creator, "Some theme",
                        createQuestion(firstQuiz,
                                QuestionType.CHECKBOX,
                                "firstQuestion",
                                "r1",
                                List.of("wrong1", "wrong2")),
                        createQuestion(firstQuiz,
                                QuestionType.RADIO,
                                "secondQuestion",
                                "r2",
                                List.of("wrong1", "wrong2", "wrong3")),
                        createQuestion(firstQuiz,
                                QuestionType.TEXT,
                                "thirdQuestion",
                                "r3",
                                List.of()),
                        createQuestion(firstQuiz,
                                QuestionType.TEXT,
                                "Fourth Question",
                                "r4",
                                List.of()),
                        createQuestion(firstQuiz,
                                QuestionType.CHECKBOX,
                                "Fifth Question",
                                "r5",
                                List.of("wrong1", "wrong2", "wrong3")),
                        createQuestion(firstQuiz,
                                QuestionType.CHECKBOX,
                                "Sixth Question",
                                "r6",
                                List.of("wrong1", "wrong2", "wrong3"))
                )
        );
        return creator;
    }

    public User generateAdmin(){
        String thirdUserName = "Gippo";
        String thirdUserPassword = "strongesteverPassword!";
        Role thirdUserRole = Role.ADMIN;
        User admin = generateUserWith(adminNickname, thirdUserName, thirdUserPassword, thirdUserRole);
        Quiz secondQuiz = new Quiz();
        admin.addQuiz(
                generateQuiz(secondQuiz, admin, "Some theme",
                        createQuestion(secondQuiz,
                                QuestionType.RADIO,
                                "secondQuestion",
                                "r2",
                                List.of("wrong1", "wrong2", "wrong3")),
                        createQuestion(secondQuiz,
                                QuestionType.CHECKBOX,
                                "Fifth Question",
                                "r5",
                                List.of("wrong1", "wrong2", "wrong3")),
                        createQuestion(secondQuiz,
                                QuestionType.CHECKBOX,
                                "Sixth Question",
                                "r6",
                                List.of("wrong1", "wrong2", "wrong3"))
                )
        );
        Quiz thirdQuiz = new Quiz();
        admin.addQuiz(
                generateQuiz(thirdQuiz, admin, "Some theme",
                        createQuestion(thirdQuiz,
                                QuestionType.CHECKBOX,
                                "firstQuestion",
                                "r1",
                                List.of("wrong1", "wrong2")),
                        createQuestion(thirdQuiz,
                                QuestionType.RADIO,
                                "secondQuestion",
                                "r2",
                                List.of("wrong1", "wrong2", "wrong3")),
                        createQuestion(thirdQuiz,
                                QuestionType.TEXT,
                                "thirdQuestion",
                                "r3",
                                List.of()),
                        createQuestion(thirdQuiz,
                                QuestionType.CHECKBOX,
                                "Fifth Question",
                                "r5",
                                List.of("wrong1", "wrong2", "wrong3"))
                )
        );
        return admin;
    }

    private User generateUserWith(String nickname, String name, String password, Role role){
        User newUser = new User();
        newUser.setNickname(nickname);
        newUser.setName(name);
        newUser.setPassword(encoder.encode(password));
        newUser.setRole(role);
        newUser.setActive(true);
        newUser.setExpired(false);
        newUser.setLocked(false);
        return newUser;
    }

    private Quiz generateQuiz(Quiz quiz, User creator, String theme, Question ...questions){
        quiz.setTheme(theme);
        for (Question question : questions){
            quiz.addQuestion(question);
        }
        quiz.setUser(creator);
        return quiz;
    }

    private Question createQuestion(Quiz quiz, QuestionType type, String content, String rightAnswer, List<String> wrongAnswers){
        Question question = new Question();
        question.setType(type);
        question.setContent(content);
        question.setRightAnswer(rightAnswer);
        question.setWrongAnswers(wrongAnswers);
        question.setQuiz(quiz);
        return question;
    }
}
