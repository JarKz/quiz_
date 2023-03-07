package com.jarkz.quiz.model;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

import com.jarkz.quiz.types.Walkthrough;

@Entity(name = "QuizWalkthrough")
@Setter
@Getter
public class QuizWalkthrough {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_walkthrough_id")
    private long id;

    @Column(unique = true, name = "nickname")
    private String nickname;

    @OneToMany(mappedBy = "quizWalkthrough", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionWalkthrough> questions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "quiz_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public void setQuestions(List<Question> questions){
        int rangeCount = questions.size() - this.questions.size();
        if (rangeCount > 0){
            addNeededQuestionWalkthrough(rangeCount);
        } else if (rangeCount < 0){
            removeManyQuestions(Math.abs(rangeCount));
        }
        int size = questions.size();
        for (int index = 0; index < size; index++){
            QuestionWalkthrough questionWalkthrough = this.questions.get(index);
            questionWalkthrough.setRealQuestion(questions.get(index));
            questionWalkthrough.setQuizWalkthrough(this);
            questionWalkthrough.setWalkthrough(Walkthrough.NOT_PASSED);
        }
    }

    private void addNeededQuestionWalkthrough(int count){
        for (int currentCount = 0; currentCount < count; currentCount++){
            this.questions.add(new QuestionWalkthrough());
        }
    }

    private void removeManyQuestions(int count){
        if (count < 0){
            count = Math.abs(count);
        }
        int size = this.questions.size();
        this.questions.subList(size - count, size).clear();
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof QuizWalkthrough q)){
            return false;
        }
        return q.getNickname().equals(this.getNickname())
            && q.getUser().equals(this.getUser())
            && q.getQuiz().equals(this.getQuiz())
            && q.getQuestions().equals(this.getQuestions());
    }

    @Override
    public int hashCode(){
        int result = nickname.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + quiz.hashCode();
        result = 31 * result + questions.hashCode();
        return result;
    }

    @Override
    public String toString(){
        return String.format("QuizWalkthrough[id=%d, nickname=%s, questions=%s, user=%s, quiz=%s]",
            id,
            nickname,
            questions.toString(),
            user.toString(),
            quiz.toString()
        );
    }
}
