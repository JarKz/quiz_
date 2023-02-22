package com.jarkz.quiz.types;

public enum Role {
    ADMIN ("ADMIN"),
    CREATOR ("CREATOR"),
    USER ("USER");

    private final String roleName;
    private Role (String roleName){
        this.roleName = roleName;
    }

    public String getRoleName(){
        return roleName;
    }
}
