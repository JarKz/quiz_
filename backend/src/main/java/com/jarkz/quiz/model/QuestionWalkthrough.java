package com.jarkz.quiz.model;

import com.jarkz.quiz.types.Walkthrough;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "QuestionWalkthrough")
@Setter
@Getter
public class QuestionWalkthrough {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(value = EnumType.STRING)
    private Walkthrough walkthrough;

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "quiz_walkthrough_id")
    private QuizWalkthrough quizWalkthrough;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public void setRealQuestion(Question question){
        this.question = question;
        question.getQuestionWalkthroughs().add(this);
    }

    public void removeQuestion(Question question){
        this.question = null;
        question.getQuestionWalkthroughs().remove(this);
    }
}
