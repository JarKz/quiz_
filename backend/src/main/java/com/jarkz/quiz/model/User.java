package com.jarkz.quiz.model;

import com.jarkz.quiz.types.Role;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity(name = "QuizUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private static final long serialVersionUID = 1427193046184337361L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "quiz_user_id")
    private long id;

    @Column(unique = true)
    private String nickname;
    private String name;
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    private boolean expired;
    private boolean locked;
    private boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "quizzes")
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private QuizWalkthrough quizWalkthrough;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(convertRole(role)));
    }

    private String convertRole(Role role){
        return "ROLE_" + role.getRoleName();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return nickname;
    }

    public boolean isAccountNonExpired() {
        return !expired;
    }

    public boolean isAccountNonLocked() {
        return !locked;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return active;
    }

    public void addQuiz(Quiz quiz){
        quizzes.add(quiz);
        quiz.setUser(this);
    }

    public void removeQuiz(Quiz quiz){
        quizzes.remove(quiz);
        quiz.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof User user)){
            return false;
        }
        return expired == user.expired
                && locked == user.locked
                && active == user.active
                && nickname.equals(user.nickname)
                && name.equals(user.name)
                && role == user.role
                && quizzes.equals(user.quizzes);
    }

    //I don't include password for check equality because it's not necessary
    //Important fields are here's
    //This also with method `hashCode`
    @Override
    public int hashCode() {
        int result = nickname.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + Boolean.hashCode(expired);
        result = 31 * result + Boolean.hashCode(locked);
        result = 31 * result + Boolean.hashCode(active);
        result = 31 * result + quizzes.hashCode();
        return result;
    }
}

