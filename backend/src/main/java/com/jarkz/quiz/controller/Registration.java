package com.jarkz.quiz.controller;

import com.jarkz.quiz.controller.forms.RegistrationForm;
import com.jarkz.quiz.model.User;
import com.jarkz.quiz.repo.UserRepo;
import com.jarkz.quiz.types.TemplateNames;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/registration")
public class Registration {

    private final PasswordEncoder encoder;
    private final UserRepo userRepo;

    public Registration(PasswordEncoder encoder, UserRepo userRepo){
        this.encoder = encoder;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String getRegistrationPage(){
        return TemplateNames.REGISTRATION.getTemplateName();
    }

    @PostMapping
    public String processRegistration(RegistrationForm form, Model model){
    	User user = userRepo.findUserByNickname(form.getNickname());
    	if (user == null) {
    		userRepo.save(form.toUser(encoder));
    		return "redirect:/login";
    	}
    	model.addAttribute("error_username", true);
    	return "registration";
    }
}
