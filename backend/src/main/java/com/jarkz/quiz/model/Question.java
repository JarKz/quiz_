package com.jarkz.quiz.model;

import com.jarkz.quiz.types.QuestionType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity(name = "Question")
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType type;
    private String content;
    @Nullable
    private List<String> wrongAnswers = new ArrayList<>();
    private List<String> rightAnswers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "question_walkthroughs")
    private List<QuestionWalkthrough> questionWalkthroughs = new ArrayList<>();

    public void addQuestionWalkthrough(QuestionWalkthrough questionWalkthrough){
        questionWalkthroughs.add(questionWalkthrough);
        questionWalkthrough.setQuestion(this);
    }

    public void removeQuestionWaklthrough(QuestionWalkthrough questionWalkthrough){
        questionWalkthroughs.remove(questionWalkthrough);
        questionWalkthrough.setQuestion(null);
    }

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (!(o instanceof Question)){
            return false;
        }
        Question q = (Question) o;
        return content.equals(q.content)
                && type.getTypeString().equals(q.type.getTypeString())
                && rightAnswers.equals(q.rightAnswers)
                && wrongAnswers.equals(q.wrongAnswers);
    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + type.getTypeString().hashCode();
        result = 31 * result + rightAnswers.hashCode();
        result = 31 * result + wrongAnswers.hashCode();
        return result;
    }

    @Override
    public String toString(){
        return String.format("Question[id=%d, type=%s, wrong_answers=%s, right_answer=%s, quiz_id=%d]",
                                    id, type.getTypeString(), wrongAnswers.toString(), rightAnswers.toString(), quiz.getId());
    }
}
