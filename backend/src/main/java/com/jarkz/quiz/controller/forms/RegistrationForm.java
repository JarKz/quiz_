package com.jarkz.quiz.controller.forms;

import com.jarkz.quiz.model.User;
import com.jarkz.quiz.types.Role;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {

    private String nickname;
    private String name;
    private String password;

    public User toUser(PasswordEncoder encoder){
        User newUser = new User();
        newUser.setName(name);
        newUser.setNickname(nickname);
        newUser.setPassword(encoder.encode(password));
        newUser.setActive(true);
        newUser.setExpired(false);
        newUser.setLocked(false);
        newUser.setRole(Role.USER);
        return newUser;
    }
}
