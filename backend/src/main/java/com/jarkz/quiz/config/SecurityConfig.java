package com.jarkz.quiz.config;

import com.jarkz.quiz.model.User;
import com.jarkz.quiz.repo.UserRepo;
import com.jarkz.quiz.types.Role;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests()
                    .requestMatchers(
                            "/quiz",
                            "/quiz-walkthrough",
                            "/walkthrough-result",
                            "/result",
                            "/scores",
                            "/personal"
                        ).hasAnyRole(Role.USER.getRoleName(), Role.CREATOR.getRoleName(), Role.ADMIN.getRoleName())
                    .requestMatchers(
                            "/quiz-management",
                            "/question-management"
                        ).hasAnyRole(Role.CREATOR.getRoleName(), Role.ADMIN.getRoleName())
                    .requestMatchers("/admin")
                        .hasRole(Role.ADMIN.getRoleName())
                    .requestMatchers("/", "/**", "/registration")
                        .permitAll()
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .usernameParameter("nickname")
                            .defaultSuccessUrl("/quiz")
                            .failureHandler((request, response, exception) -> {
                                String path = request.getContextPath();
                                String responseRedirectUrl = path + "/login?error=" + exception.getMessage();
                                response.sendRedirect(responseRedirectUrl);
                            })
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/login")
                .and()
                    .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepo repo){
        return nickname -> {
            User user = repo.findUserByNickname(nickname);
            if (user != null) {
                return user;
            }
            throw new UsernameNotFoundException("User '" + nickname + "' not found");
        };
    }
}
