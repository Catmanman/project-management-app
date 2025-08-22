package com.example.pmapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Project Management backend.  Starting the
 * Spring application context will auto-configure the web server,
 * JPA repositories and security filter chain.
 */
@SpringBootApplication
public class ProjectManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectManagementApplication.class, args);
    }
}