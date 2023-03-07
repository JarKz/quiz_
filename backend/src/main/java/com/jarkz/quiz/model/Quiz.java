package com.jarkz.quiz.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity(name = "Quiz")
@Getter
@Setter
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private long id;

    private String theme;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "questions")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy= "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "quiz_walkthroughs")
    private List<QuizWalkthrough> quizWalkthroughs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "quiz_user_id")
    private User user;

    public void addQuestion(Question question){
        questions.add(question);
        question.setQuiz(this);
    }

    public void removeQuestion(Question question){
        questions.remove(question);
        question.setQuiz(null);
    }

    public void addQuizWalkthrough(QuizWalkthrough quizWalkthrough){
        quizWalkthroughs.add(quizWalkthrough);
        quizWalkthrough.setQuiz(this);
    }

    public void removeQuizWalkthrough(QuizWalkthrough quizWalkthrough){
        quizWalkthroughs.remove(quizWalkthrough);
        quizWalkthrough.setQuiz(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz quiz)) {
            return false;
        }
        return id == quiz.id
                && theme.equals(quiz.theme)
                && questions.equals(quiz.questions);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + theme.hashCode();
        result = 31 * result + questions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (user != null){
            return String.format("Quiz[creator=%s, theme=%s, questions=%s]", user.getNickname(), theme, questions.toString());
        }
        return String.format("Quiz[creator=%s, theme=%s, questions=%s]", user, theme, questions.toString());
    }
}


