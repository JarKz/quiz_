package com.jarkz.quiz.types;

public enum Walkthrough {
    NOT_PASSED ("not_passed"),
    POSITIVE ("positive"),
    NEGATIVE ("negative");

    private final String walkthrough;

    private Walkthrough(String walkthrough){
        this.walkthrough = walkthrough;
    }

    public String getWalkthroughString(){
        return walkthrough;
    }
}
