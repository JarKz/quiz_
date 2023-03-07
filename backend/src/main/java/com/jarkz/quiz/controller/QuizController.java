package com.jarkz.quiz.controller;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.jarkz.quiz.model.*;
import com.jarkz.quiz.repo.*;
import com.jarkz.quiz.types.TemplateNames;

@Controller
@RequestMapping("/quiz")
public class QuizController {
	
	private final QuizRepo quizRepo;
	private final QuestionWalkthroughRepo questionWalkthroughRepo;
	private final QuizWalkthroughRepo quizWalkthroughRepo;

	private final int pageSize = 20;
	
	public QuizController(
		QuizRepo quizRepo,
		QuestionWalkthroughRepo questionWalkthroughRepo,
		QuizWalkthroughRepo quizWalkthroughRepo
	) {
		this.quizRepo = quizRepo;
		this.questionWalkthroughRepo = questionWalkthroughRepo;
		this.quizWalkthroughRepo = quizWalkthroughRepo;
	}
	
	@GetMapping
	public String getPageWithFirst20Quizzes(
		@RequestParam(name = "page", required = false, defaultValue = "1") int currentPage,
		Model model
	) {
		if (currentPage < 1){
			currentPage = 1;
			model.addAttribute("warning", "Please, don't get pages before first!");
		}
		int toZeroBasedPage = currentPage > 0 ? currentPage - 1 : currentPage;
		Page<Quiz> quizzesSliceAsPage = quizRepo.findAllByQuizWhereQuestionNotEmpty(PageRequest.of(toZeroBasedPage, pageSize));
		if (quizzesSliceAsPage.isEmpty() && toZeroBasedPage > 0){
			toZeroBasedPage = quizzesSliceAsPage.getTotalPages() - 1;
			currentPage = toZeroBasedPage + 1;
			quizzesSliceAsPage = quizRepo.findAllByQuizWhereQuestionNotEmpty(PageRequest.of(toZeroBasedPage, pageSize));
			model.addAttribute("warning", "Please, don't get pages after last!");
		}
		List<Quiz> quizzesSlice = quizzesSliceAsPage.getContent();
		model.addAttribute("quizzes", quizzesSlice);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", quizzesSliceAsPage.getTotalPages());
		return TemplateNames.QUIZ.getTemplateName();
	}

	@GetMapping("/pass-quiz")
	public String passQuiz(
		@RequestParam(name = "quiz_id") long quizId,
		Authentication auth,
		Model model
	){
		Quiz quiz = quizRepo.getQuizWithQuestionsById(quizId);
		String nickname = auth.getName();
		QuizWalkthrough quizWalkthrough = quizWalkthroughRepo.findQuizWalkthroughByNickname(nickname);
		boolean toSaveFirst = false;
		if (quizWalkthrough == null){
			quizWalkthrough = new QuizWalkthrough();
			toSaveFirst = true;
		}
		quizWalkthrough.setNickname(nickname);
		quizWalkthrough.setQuiz(quiz);
		quizWalkthrough.setQuestions(quiz.getQuestions());
		quizWalkthrough.setUser(quiz.getUser());
		if (toSaveFirst){
			quizWalkthroughRepo.save(quizWalkthrough);
			questionWalkthroughRepo.saveAll(quizWalkthrough.getQuestions());
		} else {
			questionWalkthroughRepo.saveAll(quizWalkthrough.getQuestions());
			quizWalkthroughRepo.save(quizWalkthrough);
		}
		return String.format("redirect:/%s", TemplateNames.QUIZ_WALKTHROUGH.getResourseName());
	}
}