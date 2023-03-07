package com.jarkz.quiz.controller;

import com.jarkz.quiz.repo.*;
import com.jarkz.quiz.types.*;
import com.jarkz.quiz.types.model.QuestionResult;
import com.jarkz.quiz.model.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class QuizWalkthroughController {

    private final QuizWalkthroughRepo quizWalkthroughRepo;
    private final QuestionWalkthroughRepo questionWalkthroughRepo;

    public QuizWalkthroughController(
        QuizWalkthroughRepo quizWalkthroughRepo,
        QuestionWalkthroughRepo questionWalkthroughRepo
    ){
        this.quizWalkthroughRepo = quizWalkthroughRepo;
        this.questionWalkthroughRepo = questionWalkthroughRepo;
    }

    @GetMapping("/quiz-walkthrough")
    public String quizWalkthrough(
        Authentication auth,
        Model model
    ){
        String nickname = auth.getName();
        String nextTemplate = nextQuestionOrRedirect(nickname, model);
        return nextTemplate;
    }

    @PostMapping("/quiz-walkthrough")
    public String quizWalkthroughGetAnswerAndReturnPage(
        @RequestParam(name = "question_walkthrough_id") long questionWalkthroughId,
        @RequestParam(name = "answer") List<String> answer,
        Authentication auth,
        Model model
    ){
        checkAnswerAndSaveResult(questionWalkthroughId, answer);
        String nickname = auth.getName();
        String nextTemplate = nextQuestionOrRedirect(nickname, model);
        return nextTemplate;
    }

    @GetMapping("/walkthrough-result")
    public String returnResultPage(
        Authentication auth,
        Model model
    ){
        String nickname = auth.getName();
        QuizWalkthrough quizWalkthrough = quizWalkthroughRepo.getQuizWalkthroughWithQuestionsAndRealQuizByNickname(nickname);
        List<QuestionWalkthrough> questions = quizWalkthrough.getQuestions();
        model.addAttribute("theme", quizWalkthrough
            .getQuiz()
            .getTheme()
        );
        model.addAttribute("total_questions", questions.size());
        model.addAttribute("passed_questions", questions.stream()
            .filter(question -> question.getWalkthrough() == Walkthrough.POSITIVE)
            .toList()
            .size()
        );
        return TemplateNames.WALKTHROUGH_RESULT.getTemplateName();
    }

    @GetMapping("/result")
    public String returnResultList(
        Authentication auth,
        Model model
    ){
        String nickname = auth.getName();
        QuizWalkthrough quizWalkthrough = quizWalkthroughRepo.getQuizWalkthroughWithQuestionsAndRealQuizByNickname(nickname);
        model.addAttribute("theme", quizWalkthrough.getQuiz().getTheme());
        List<Long> questionWalkthroughIds = quizWalkthrough.getQuestions()
            .stream()
            .map(QuestionWalkthrough::getId)
            .toList();
        List<QuestionWalkthrough> questions = questionWalkthroughRepo.getQuestionWalkthroughWithRealQuestionsByIds(questionWalkthroughIds);
        List<QuestionResult> questionResultList = new ArrayList<>();
        for (QuestionWalkthrough questionWalkthrough : questions){
            List<String> wrongAnswers = questionWalkthrough.getQuestion().getWrongAnswers();
            List<String> answers = new ArrayList<>();
            if (wrongAnswers != null){
                answers.addAll(wrongAnswers);
            }
            answers.addAll(questionWalkthrough.getQuestion().getRightAnswers());
            questionResultList.add(new QuestionResult(
                questionWalkthrough.getQuestion().getContent(),
                answers,
                questionWalkthrough.getAnswer(),
                questionWalkthrough.getQuestion().getRightAnswers(),
                questionWalkthrough.getQuestion().getType().getTypeString(),
                questionWalkthrough.getWalkthrough().getWalkthroughString()
            ));
        }
        model.addAttribute("question_results", questionResultList);
        return TemplateNames.RESULT.getTemplateName();
    }

    private String nextQuestionOrRedirect(String nickname, Model model){
        QuizWalkthrough quizWalkthrough = quizWalkthroughRepo.getQuizWalkthroughWithQuestionsAndRealQuizByNickname(nickname);
        if (quizWalkthrough == null){
            return String.format("redirect:/", TemplateNames.QUIZ.getResourseName());
        }
        List<QuestionWalkthrough> questions = quizWalkthrough.getQuestions();
        Optional<QuestionWalkthrough> probableQuestionWalkthrough = getNextQuestionWalkthrough(questions);
        if (probableQuestionWalkthrough.isPresent()){
            QuestionWalkthrough nextQuestionToWalkthrough = probableQuestionWalkthrough.get();
            model.addAttribute("question_walkthrough_id", nextQuestionToWalkthrough.getId());

            Question nextQuestion = nextQuestionToWalkthrough.getQuestion();
            model.addAttribute("question_content", nextQuestion.getContent());
            model.addAttribute("question_type", nextQuestion.getType());

            List<String> wrongAnswers = nextQuestion.getWrongAnswers();
            List<String> answers = new ArrayList<>();
            if (wrongAnswers != null){
                answers.addAll(wrongAnswers);
            }
            answers.addAll(nextQuestion.getRightAnswers());
            model.addAttribute("question_answers", answers);
        } else {
            return String.format("redirect:/%s", TemplateNames.WALKTHROUGH_RESULT.getResourseName());
        }
        model.addAttribute("theme", quizWalkthrough.getQuiz().getTheme());
        return TemplateNames.QUIZ_WALKTHROUGH.getTemplateName();
    }

    private void checkAnswerAndSaveResult(long questionWalkthroughId, List<String> answer){
        if (questionWalkthroughId == 0 || answer == null){
            return;
        }
        Optional<QuestionWalkthrough> probableQuestion = questionWalkthroughRepo.getQuestionWalkthroughWithRealQuestionById(questionWalkthroughId);
        if (probableQuestion.isPresent()){
            QuestionWalkthrough questionWalkthrough = probableQuestion.get();
            Question realQuestion = questionWalkthrough.getQuestion();
            if (realQuestion.getRightAnswers().containsAll(answer)){
                questionWalkthrough.setWalkthrough(Walkthrough.POSITIVE);
            } else {
                questionWalkthrough.setWalkthrough(Walkthrough.NEGATIVE);
            }
            questionWalkthrough.setAnswer(answer.toString());
            questionWalkthroughRepo.save(questionWalkthrough);
        }
    }

    private Optional<QuestionWalkthrough> getNextQuestionWalkthrough(List<QuestionWalkthrough> questions){
        if (questions.isEmpty()){
            return Optional.empty();
        }
        List<QuestionWalkthrough> notPassedQuestions = questions.stream()
                                                                .filter(q -> q.getWalkthrough() == Walkthrough.NOT_PASSED)
                                                                .toList();
        if (notPassedQuestions.size() == 0){
            return Optional.ofNullable(null);
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomNotPassedQuestionIndex = random.nextInt(0, notPassedQuestions.size());
        QuestionWalkthrough nextQuestionToWalkthrough = notPassedQuestions.get(randomNotPassedQuestionIndex);
        return questionWalkthroughRepo.getQuestionWalkthroughWithRealQuestionById(nextQuestionToWalkthrough.getId());
    }
}