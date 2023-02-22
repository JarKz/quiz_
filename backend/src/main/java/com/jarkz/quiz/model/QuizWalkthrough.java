package com.jarkz.quiz.model;

import com.jarkz.quiz.types.*;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "QuizWalkthrough")
@Setter
@Getter
public class QuizWalkthrough {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, name = "nickname")
    private String nickname;

    @Column(name = "questions")
    private List<Integer> questionIds;
    @Column(name = "walkthrough_questions")
    private List<Walkthrough> walkthroughQuestions;

    @OneToOne
    @JoinColumn(name = "quiz_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Override
    public String toString(){
        return String.format("QuizWalkthrough[id=%d, nickname=%s, questions=%s, walkthrough-questions=%s]",
            id,
            nickname,
            questionIds.toString(),
            walkthroughQuestions.toString()
        );
    }
}
