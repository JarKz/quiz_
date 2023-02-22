package com.jarkz.quiz.config;

import com.jarkz.quiz.types.TemplateNames;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName(TemplateNames.HOME.getTemplateName());
    }
}
