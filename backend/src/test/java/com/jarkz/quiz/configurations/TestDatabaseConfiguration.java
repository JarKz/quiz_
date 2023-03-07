package com.jarkz.quiz.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.jarkz.quiz.repo"
})
@EnableTransactionManagement
public class TestDatabaseConfiguration {

    @Value("${test.db_user}")
    private String dbUser;
    @Value("${test.db_password}")
    private String dbPassword;

    @Bean
    @Profile("test")
    public DataSource dataSource(){
        return DataSourceBuilder
                .create()
                .driverClassName("org.postgresql.Driver")
                .username(dbUser)
                .password(dbPassword)
                .url("jdbc:postgresql://localhost:5432/test")
                .build();
    }
}
