package com.jarkz.quiz.controller;

import com.jarkz.quiz.types.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {

    @GetMapping("/home")
    public String getHomepage(){
        return TemplateNames.HOME.getTemplateName();
    }
}
