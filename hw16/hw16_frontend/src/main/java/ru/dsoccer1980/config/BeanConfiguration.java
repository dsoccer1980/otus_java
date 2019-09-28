package ru.dsoccer1980.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.service.FrontendMessageSystemClient;
import ru.dsoccer1980.service.FrontendMessageSystemClientImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class BeanConfiguration {

    @Value("${address.frontend}")
    private String frontendAddress;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public FrontendMessageSystemClient getFrontendMessageSystemClient(MessageSystem messageSystem) {
        FrontendMessageSystemClient frontendMessageSystemClient = new FrontendMessageSystemClientImpl(messageSystem);
        frontendMessageSystemClient.setAddress(new Address(frontendAddress));
        messageSystem.addMessageSystemClient(frontendMessageSystemClient);
        return frontendMessageSystemClient;
    }

}
