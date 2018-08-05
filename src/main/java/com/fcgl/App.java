/*
 * Starter.
 */
package com.fcgl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        SpringApplication.run(App.class, args);

            System.out.println(new App().getGreeting());
    }
}
