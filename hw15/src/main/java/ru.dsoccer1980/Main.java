package ru.dsoccer1980;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.messageSystem.MessageSystemClient;
import ru.dsoccer1980.service.FrontendServiceMessageSystemClient;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        MessageSystem messageSystem = context.getBean(MessageSystem.class);
        messageSystem.addMessageSystemClient(context.getBean(FrontendServiceMessageSystemClient.class));
        messageSystem.addMessageSystemClient((MessageSystemClient) context.getBean(JdbcTemplate.class));

        messageSystem.start();
    }
}
