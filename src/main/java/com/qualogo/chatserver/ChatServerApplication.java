package com.qualogo.chatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The ChatServerApplication class serves as the entry point for the Spring Boot application.
 * It contains the main method which is the starting point of the Java application.
 */
@SpringBootApplication
public class ChatServerApplication {

    /**
     * The main method which serves as the entry point for the Spring Boot application.
     * It delegates to Spring Boot's SpringApplication class by calling run.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ChatServerApplication.class, args);
    }

}