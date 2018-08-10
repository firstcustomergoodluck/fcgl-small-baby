/*
 * Starter.
 */
package com.fcgl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.endpoints"})//TODO: See if there is a better way of doing this
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
