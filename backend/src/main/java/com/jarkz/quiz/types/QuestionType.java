package com.jarkz.quiz.types;

public enum QuestionType {
    CHECKBOX ("checkbox"),
    RADIO ("radio"),
    TEXT ("text");

    private String type;

    private QuestionType(String type){
        this.type = type;
    }

    public String getTypeString(){
        return type;
    }
}
