package ru.dsoccer1980.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.CacheEngineImpl;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.messageSystem.MessageSystemImpl;
import ru.dsoccer1980.service.*;

@Configuration
@PropertySource("classpath:application.properties")
public class BeanConfiguration {

    @Value("${address.dbservice}")
    private String dbServiceAddress;

    @Value("${address.frontend}")
    private String frontendAddress;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CacheEngine<Long, User> getCacheEngine() {
        return new CacheEngineImpl<>(5, 1000, 0, false);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return HibernateUtils.getSessionFactory("hibernate.cfg.xml", User.class);
    }

    @Bean
    public MessageSystem getMessageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public DBServiceMessageSystemClient getDBServiceMessageSystemClient() {
        DBServiceMessageSystemClientImpl dbServiceMessageSystemClient = new DBServiceMessageSystemClientImpl(getSessionFactory(), getCacheEngine(), getMessageSystem());
        dbServiceMessageSystemClient.setAddress(new Address(dbServiceAddress));
        getMessageSystem().addMessageSystemClient(dbServiceMessageSystemClient);
        return dbServiceMessageSystemClient;
    }

    @Bean
    public FrontendMessageSystemClient getFrontendMessageSystemClient() {
        FrontendMessageSystemClientImpl frontendMessageSystemClient = new FrontendMessageSystemClientImpl(getMessageSystem());
        frontendMessageSystemClient.setAddress(new Address(frontendAddress));
        getMessageSystem().addMessageSystemClient(frontendMessageSystemClient);
        return frontendMessageSystemClient;
    }


}
