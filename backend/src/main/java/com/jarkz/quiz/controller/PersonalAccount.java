package com.jarkz.quiz.controller;

import com.jarkz.quiz.types.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/personal-account")
public class PersonalAccount {

    @GetMapping
    public String getPersonalPage(){
        return TemplateNames.PERSONAL_ACCOUNT.getTemplateName();
    }

}
