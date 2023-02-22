package com.jarkz.quiz.model;

import com.jarkz.quiz.model.generator.EntitiesGenerator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootTest
public class TestEqualsAndHashCodeEntities {

    private final EntitiesGenerator generator;

    @Autowired
    public TestEqualsAndHashCodeEntities(
            PasswordEncoder encoder
    ){
        final String USER_NICKNAME = "balbsa";
        final String CREATOR_NICKNAME = "abasbd";
        final String ADMIN_NICKNAME = "BIBAdf";

        generator = new EntitiesGenerator(
                USER_NICKNAME,
                CREATOR_NICKNAME,
                ADMIN_NICKNAME,
                encoder
        );
    }

    @Test
    public void testUserEquals(){
        User user1 = generator.generateUser();
        User user2 = generator.generateUser();
        User user3 = generator.generateUser();
        User user4 = generator.generateUser();
        user4.setName("anotherName");
        // Reflexivity
        Assertions.assertEquals(user1, user1);

        // Symmetry
        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user2, user1);

        // Transitivity
        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user2, user3);
        Assertions.assertEquals(user1, user3);

        // Consistency
        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user1, user2);

        // Non-nullity
        Assertions.assertNotEquals(user1, null);

        //checking not right
        Assertions.assertNotEquals(user1, user4);
        Assertions.assertNotEquals(user2, user4);
        Assertions.assertNotEquals(user3, user4);
    }

    @Test
    public void testHashCodeUser(){
        User user1 = generator.generateUser();
        User user2 = generator.generateUser();
        User user3 = generator.generateUser();
        user3.setName("anotherName");

        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user1.hashCode(), user2.hashCode());

        Assertions.assertNotEquals(user1, user3);
        Assertions.assertNotEquals(user2, user3);
        Assertions.assertNotEquals(user1.hashCode(), user3.hashCode());
        Assertions.assertNotEquals(user2.hashCode(), user3.hashCode());

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        Assertions.assertEquals(users.size(), 1);
        users.add(user3);
        Assertions.assertEquals(users.size(), 2);
    }

    @Test
    public void testCreatorEquals(){
        User creator1 = generator.generateCreator();
        User creator2 = generator.generateCreator();
        User creator3 = generator.generateCreator();
        User creator4 = generator.generateCreator();
        creator4.setName("anotherName");
        // Reflexivity
        Assertions.assertEquals(creator1, creator1);

        // Symmetry
        Assertions.assertEquals(creator1, creator2);
        Assertions.assertEquals(creator2, creator1);

        // Transitivity
        Assertions.assertEquals(creator1, creator2);
        Assertions.assertEquals(creator2, creator3);
        Assertions.assertEquals(creator1, creator3);

        // Consistency
        Assertions.assertEquals(creator1, creator2);
        Assertions.assertEquals(creator1, creator2);
        Assertions.assertEquals(creator1, creator2);

        // Non-nullity
        Assertions.assertNotEquals(creator1, null);

        //checking not right
        Assertions.assertNotEquals(creator1, creator4);
        Assertions.assertNotEquals(creator2, creator4);
        Assertions.assertNotEquals(creator3, creator4);
    }

    @Test
    public void testHashCodeCreator(){
        User creator1 = generator.generateCreator();
        User creator2 = generator.generateCreator();
        User creator3 = generator.generateCreator();
        creator3.setName("anotherName");

        Assertions.assertEquals(creator1, creator2);
        Assertions.assertEquals(creator1.hashCode(), creator2.hashCode());

        Assertions.assertNotEquals(creator1, creator3);
        Assertions.assertNotEquals(creator2, creator3);
        Assertions.assertNotEquals(creator1.hashCode(), creator3.hashCode());
        Assertions.assertNotEquals(creator2.hashCode(), creator3.hashCode());

        Set<User> users = new HashSet<>();
        users.add(creator1);
        users.add(creator2);
        Assertions.assertEquals(users.size(), 1);
        users.add(creator3);
        Assertions.assertEquals(users.size(), 2);
    }
    @Test
    public void testAdminEquals(){
        User admin1 = generator.generateAdmin();
        User admin2 = generator.generateAdmin();
        User admin3 = generator.generateAdmin();
        User admin4 = generator.generateAdmin();
        admin4.setName("anotherName");
        // Reflexivity
        Assertions.assertEquals(admin1, admin1);

        // Symmetry
        Assertions.assertEquals(admin1, admin2);
        Assertions.assertEquals(admin2, admin1);

        // Transitivity
        Assertions.assertEquals(admin1, admin2);
        Assertions.assertEquals(admin2, admin3);
        Assertions.assertEquals(admin1, admin3);

        // Consistency
        Assertions.assertEquals(admin1, admin2);
        Assertions.assertEquals(admin1, admin2);
        Assertions.assertEquals(admin1, admin2);

        // Non-nullity
        Assertions.assertNotEquals(admin1, null);

        //checking not right
        Assertions.assertNotEquals(admin1, admin4);
        Assertions.assertNotEquals(admin2, admin4);
        Assertions.assertNotEquals(admin3, admin4);
    }

    @Test
    public void testHashCodeAdmin(){
        User admin1 = generator.generateAdmin();
        User admin2 = generator.generateAdmin();
        User admin3 = generator.generateAdmin();
        admin3.setName("anotherName");

        Assertions.assertEquals(admin1, admin2);
        Assertions.assertEquals(admin1.hashCode(), admin2.hashCode());

        Assertions.assertNotEquals(admin1, admin3);
        Assertions.assertNotEquals(admin2, admin3);
        Assertions.assertNotEquals(admin1.hashCode(), admin3.hashCode());
        Assertions.assertNotEquals(admin2.hashCode(), admin3.hashCode());

        Set<User> users = new HashSet<>();
        users.add(admin1);
        users.add(admin2);
        Assertions.assertEquals(users.size(), 1);
        users.add(admin3);
        Assertions.assertEquals(users.size(), 2);
    }
}
