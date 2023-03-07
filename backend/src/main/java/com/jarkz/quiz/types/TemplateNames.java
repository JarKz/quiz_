package com.jarkz.quiz.types;

public enum TemplateNames {
    HOME ("home"),
    LOGIN ("sign/login"),
    REGISTRATION ("sign/registration"),
    QUIZ_MANAGEMENT ("management/quiz-management"),
    QUESTION_MANAGEMENT ("management/question-management"),
    QUIZ ("quiz"),
    QUIZ_WALKTHROUGH ("walkthrough/quiz-walkthrough"),
    WALKTHROUGH_RESULT("walkthrough/walkthrough-result"),
    RESULT("walkthrough/result"),
    PERSONAL_ACCOUNT ("personal");

    private final String templateName;

    TemplateNames(String templateName){
        this.templateName = templateName;
    }

    public String getTemplateName(){
        return templateName;
    }

    public String getResourseName(){
        String[] splitedTemplateName = templateName.split("/");
        return splitedTemplateName.length == 1 ? splitedTemplateName[0] : splitedTemplateName[1];
    }
}
