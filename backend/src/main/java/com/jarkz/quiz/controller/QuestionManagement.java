package com.jarkz.quiz.controller;

import com.jarkz.quiz.model.*;
import com.jarkz.quiz.repo.*;
import com.jarkz.quiz.types.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/question-management")
public class QuestionManagement {

	private final QuizRepo quizRepo;
	private final QuestionRepo questionRepo;

	public QuestionManagement(
			QuizRepo quizRepo,
			QuestionRepo questionRepo,
			UserRepo userRepo
	){
		this.quizRepo = quizRepo;
		this.questionRepo = questionRepo;
	}

	@GetMapping
	public String reloadPage(
		@RequestParam(name = "quiz_id") long quizId,
		Authentication auth,
		Model model
	){
		Quiz quiz = quizRepo.getQuizWithUserById(quizId);
		if (quiz.getUser() != null && quiz.getUser().getNickname().equals(auth.getName())) {
			quiz = quizRepo.getQuizWithQuestionsById(quizId);
			model.addAttribute("quiz", quiz);
			return TemplateNames.QUESTION_MANAGEMENT.getTemplateName();
		}
		return String.format("redirect:/%s", TemplateNames.QUIZ.getResourseName());
	}

	@PostMapping
	public String getPage(
		@RequestParam(name = "id") long quizId,
		Authentication auth,
		Model model
	){
		Quiz quiz = quizRepo.getQuizWithUserById(quizId);
		if (quiz.getUser() != null && quiz.getUser().getNickname().equals(auth.getName())) {
			quiz = quizRepo.getQuizWithQuestionsById(quizId);
			model.addAttribute("quiz", quiz);
			return TemplateNames.QUESTION_MANAGEMENT.getTemplateName();
		}
		return String.format("redirect:/%s", TemplateNames.QUIZ.getResourseName());
	}

	@PostMapping("/create")
	public String createQuestionAndReturnNewPage(
		@RequestParam(name = "quiz_id") long quizId,
		@RequestParam(name = "type") String type,
		@RequestParam(name = "content") String content,
		@RequestParam(name = "right-answers") List<String> rightAnswers,
		@RequestParam(name = "wrong-answers", required = false) List<String> wrongAnswers,
		Authentication auth
	){
		Quiz quiz = quizRepo.getQuizWithUserById(quizId);
		if (quiz.getUser() != null && quiz.getUser().getNickname().equals(auth.getName())) {
			Question question = new Question();
			question.setType(QuestionType.valueOf(type.toUpperCase()));
			question.setContent(content);
			question.setRightAnswers(rightAnswers);
			question.setWrongAnswers(wrongAnswers);
			quiz.addQuestion(question);
			quizRepo.save(quiz);
			return String.format("redirect:/%s?quiz_id=%d", TemplateNames.QUESTION_MANAGEMENT.getResourseName(), quizId);
		}
		return String.format("redirect:/%s", TemplateNames.QUIZ.getResourseName());
	}

	@PostMapping("/edit")
	public String createQuestionAndReturnNewPage(
		@RequestParam(name = "quiz_id") long quizId,
		@RequestParam(name = "question_id") long questionId,
		@RequestParam(name = "type") String type,
		@RequestParam(name = "content") String content,
		@RequestParam(name = "right-answers") List<String> rightAnswers,
		@RequestParam(name = "wrong-answers", required = false) List<String> wrongAnswers,
		Authentication auth
	){
		Quiz quiz = quizRepo.getQuizWithUserById(quizId);
		if (quiz.getUser() != null && quiz.getUser().getNickname().equals(auth.getName())) {
			Question question = questionRepo.findQuestionById(questionId);
			question.setType(QuestionType.valueOf(type.toUpperCase()));
			question.setContent(content);
			question.setRightAnswers(rightAnswers);
			question.setWrongAnswers(wrongAnswers);
			questionRepo.save(question);
			return String.format("redirect:/%s?quiz_id=%d", TemplateNames.QUESTION_MANAGEMENT.getResourseName(), quizId);
		}
		return String.format("redirect:/%s", TemplateNames.QUIZ.getResourseName());
	}

	@PostMapping("/delete")
	public String deleteQuestionAndReturnNewPage(
		@RequestParam(name = "quiz_id") long quizId,
		@RequestParam(name = "question_id") long questionId,
		Authentication auth
	){
		Quiz quiz = quizRepo.getQuizWithUserById(quizId);
		if (quiz.getUser() != null && quiz.getUser().getNickname().equals(auth.getName())) {
			questionRepo.deleteById(questionId);
			return String.format("redirect:/%s?quiz_id=%d", TemplateNames.QUESTION_MANAGEMENT.getResourseName(), quizId);
		}
		return String.format("redirect:/%s", TemplateNames.QUIZ.getResourseName());
	}
}
