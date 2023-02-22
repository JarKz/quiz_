package com.jarkz.quiz.controller;

import com.jarkz.quiz.model.*;
import com.jarkz.quiz.repo.*;
import com.jarkz.quiz.types.TemplateNames;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quiz-management")
public class QuizManagement {

    private final UserRepo userRepo;
    private final QuizRepo quizRepo;

    public QuizManagement(
            UserRepo userRepo,
            QuizRepo quizRepo
    ){
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
    }

    @GetMapping
    public String getQuizPage(Authentication auth, Model model){
        String nickname = auth.getName();
        model.addAttribute("user", userRepo.getUserWithQuizzesByNickname(nickname));
        return TemplateNames.QUIZ_MANAGEMENT.getTemplateName();
    }

    @PostMapping("/create")
    public String createQuizAndReturnQuizPage(
            @RequestParam(name = "theme", defaultValue = "No theme") String theme,
            Authentication auth
    ){
        String nickname = auth.getName();
        User user = userRepo.getUserWithQuizzesByNickname(nickname);
        Quiz quiz = new Quiz();
        quiz.setTheme(theme);
        quiz.setUser(user);
        user.addQuiz(quiz);
        userRepo.save(user);
        return "redirect:/" + TemplateNames.QUIZ_MANAGEMENT.getTemplateName();
    }

    @PostMapping("/edit")
    public String editQuizAndReturnQuizPage(
            @RequestParam(name = "id") long quizId,
            @RequestParam(name = "theme", defaultValue = "No theme") String theme,
            Authentication auth
    ){
    	Quiz quiz = quizRepo.getQuizWithUserById(quizId);
    	if (quiz.getUser() != null && quiz.getUser().getNickname().equals(auth.getName())){
    		quiz.setTheme(theme);
    		quizRepo.save(quiz);
    		quizRepo.flush();
    	}
    	return "redirect:/" + TemplateNames.QUIZ_MANAGEMENT.getTemplateName();
    }

    @PostMapping("/delete")
    public String deleteQuizAndReturnQuizPage(
            @RequestParam(name = "id") long quizId,
            Authentication auth
    ){
    	Quiz quiz = quizRepo.getQuizWithUserById(quizId);
    	if (quiz.getUser() != null && quiz.getUser().getNickname().equals(auth.getName())){
	        quizRepo.deleteById(quizId);
	        quizRepo.flush();
    	}
        return "redirect:/" + TemplateNames.QUIZ_MANAGEMENT.getTemplateName();
    }
}
