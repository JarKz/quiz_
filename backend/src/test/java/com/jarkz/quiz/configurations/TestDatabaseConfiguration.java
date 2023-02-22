package com.jarkz.quiz.configurations;

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

    @Bean
    @Profile("test")
    public DataSource dataSource(){
        return DataSourceBuilder
                .create()
                .driverClassName("org.postgresql.Driver")
                .username("jarkz")
                .password("fjanJA932jLfa].")
                .url("jdbc:postgresql://localhost:5432/test")
                .build();
    }
}
