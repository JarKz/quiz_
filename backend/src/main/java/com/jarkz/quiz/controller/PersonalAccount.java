package com.jarkz.quiz.controller;

import com.jarkz.quiz.model.User;
import com.jarkz.quiz.repo.UserRepo;
import com.jarkz.quiz.types.*;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/personal")
public class PersonalAccount {
    
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public PersonalAccount(
        UserRepo userRepo,
        PasswordEncoder passwordEncoder
    ){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getPersonalPage(
        @RequestParam(name = "error", required = false, defaultValue = "") String error,
        Authentication auth,
        Model model
    ){
        String nickname = auth.getName();
        User user = userRepo.findUserByNickname(nickname);
        model.addAttribute("user", user);

        if (!error.isEmpty()){
            model.addAttribute("error", error);
        } else {
            model.addAttribute("error", null);
        }

        return TemplateNames.PERSONAL_ACCOUNT.getTemplateName();
    }

    @PostMapping("/update-name-or-nickname")
    public String changeNameOrNickname(
        @RequestParam(name = "nickname", defaultValue = "") String nicknameToChange,
        @RequestParam(name = "name", defaultValue = "") String nameToChange,
        Authentication auth
    ){
        String nickname = auth.getName();
        if (nicknameToChange.isEmpty()){
            return redirectToPersonalAccountWithError("Nickname is empty, please try another");
        }

        if (nickname.equals(nicknameToChange)) {
            User currentUser = userRepo.findUserByNickname(nickname);
            currentUser.setName(nameToChange);
            userRepo.save(currentUser);
            return redirectToPersonalAccount();
        }

        User probableUser = userRepo.findUserByNickname(nicknameToChange);
        if (probableUser == null){
            User currentUser = userRepo.findUserByNickname(nickname);
            currentUser.setNickname(nicknameToChange);
            currentUser.setName(nameToChange);
            userRepo.save(currentUser);

            Authentication reAuth = new UsernamePasswordAuthenticationToken(nicknameToChange, currentUser.getPassword(), auth.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(reAuth);

            return redirectToPersonalAccount();
        }
        return redirectToPersonalAccountWithError("Nickname is exists, please try another");
    }

    @PostMapping("/update-password")
    public String updatePassword(
        @RequestParam(name = "old-password") String oldPassword,
        @RequestParam(name = "new-password") String newPassword,
        @RequestParam(name = "new-password-again") String newPasswordAgain,
        @RequestParam(name = "nickname") String nicknameFromRequest,
        Authentication auth
    ){
        String nickname = auth.getName();
        if (nicknameFromRequest.isEmpty() || !nickname.equals(nicknameFromRequest)){
            return redirectToPersonalAccountWithError("Reload this page and try again, please");
        }

        if (oldPassword.isEmpty()){
            return redirectToPersonalAccountWithError("Old password is empty");
        }
        User currentUser = userRepo.findUserByNickname(nickname);
        String currentPassword = currentUser.getPassword();
        if (!passwordEncoder.matches(oldPassword, currentPassword)){
            return redirectToPersonalAccountWithError("Old password is not right");
        }

        if (newPassword.equals(newPasswordAgain)){
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(currentUser);
        } else {
            return redirectToPersonalAccountWithError("Password not equals, please try again");
        }
        return redirectToPersonalAccount();
    }

    @PostMapping("/update-role")
    public String updateRole(
        @RequestParam(name = "nickname") String nicknameFromRequest,
        @RequestParam(name = "role") String roleAsString,
        Authentication auth
    ){
        String nickname = auth.getName();
        if (nicknameFromRequest.isEmpty() || !nickname.equals(nicknameFromRequest)){
            return redirectToPersonalAccountWithError("Reload this page and try again, please");
        }

        User currentUser = userRepo.findUserByNickname(nickname);
        if (currentUser.getRole() == Role.ADMIN){
            return redirectToPersonalAccountWithError("You're admin, you don't need to change role");
        }

        final String upperCasedRoleAsString = roleAsString.toUpperCase();
        if (Arrays.stream(Role.values()).filter(r -> r.getRoleName().equals(upperCasedRoleAsString)).count() != 1){
            return redirectToPersonalAccountWithError("Reload this page and try again, please");
        }
        Role role = Role.valueOf(upperCasedRoleAsString);
        currentUser.setRole(role);
        userRepo.save(currentUser);
        Authentication reAuth = new UsernamePasswordAuthenticationToken(currentUser.getNickname(), currentUser.getPassword(), currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(reAuth);

        return redirectToPersonalAccount();
    }

    private String redirectToPersonalAccount(){
        return String.format("redirect:/%s", TemplateNames.PERSONAL_ACCOUNT.getResourseName());
    }

    private String redirectToPersonalAccountWithError(String error){
        return String.format("redirect:/%s?error=%s", TemplateNames.PERSONAL_ACCOUNT.getResourseName(), error);
    }
}
