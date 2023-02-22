package com.jarkz.quiz.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jarkz.quiz.model.Quiz;
import com.jarkz.quiz.repo.QuizRepo;
import com.jarkz.quiz.types.TemplateNames;

@Controller
@RequestMapping("/quiz")
public class QuizController {
	
	private final QuizRepo quizRepo;

	private final int pageSize = 20;
	
	public QuizController(
		QuizRepo quizRepo
	) {
		this.quizRepo = quizRepo;
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
}
