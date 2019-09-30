package ru.dsoccer1980;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.dsoccer1980.messageSystem", "ru.dsoccer1980.config"})
public class DbMain {

    public static void main(String[] args) {
        SpringApplication.run(ru.dsoccer1980.DbMain.class);
    }

}
