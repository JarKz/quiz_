package com.jarkz.quiz.types;

public enum TemplateNames {
    HOME ("home"),
    LOGIN ("login"),
    REGISTRATION ("registration"),
    QUIZ_MANAGEMENT ("quiz-management"),
    QUESTION_MANAGEMENT ("question-management"),
    QUIZ ("quiz"),
    PERSONAL_ACCOUNT ("personal-account");

    private final String templateName;

    TemplateNames(String templateName){
        this.templateName = templateName;
    }

    public String getTemplateName(){
        return templateName;
    }
}
