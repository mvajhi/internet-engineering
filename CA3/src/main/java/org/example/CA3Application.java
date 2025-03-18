package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CA3Application {

    public static void main(String[] args) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
//        consoleHandler.run();
        SpringApplication.run(CA3Application.class, args);
    }

}
