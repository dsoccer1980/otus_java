package ru.dsoccer1980.config;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.CacheEngineImpl;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.service.DBServiceMessageSystemClient;
import ru.dsoccer1980.service.DBServiceMessageSystemClientImpl;
import ru.dsoccer1980.service.HibernateUtils;

@Configuration
@PropertySource("classpath:application.properties")
public class BeanConfiguration {

    @Value("${address.dbservice}")
    private String dbServiceAddress;

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
    public JdbcTemplate<User> getJdbcTemplate(CacheEngine<Long, User> cache, SessionFactory sessionFactory) {
        return new HibernateImpl<>(sessionFactory, cache);
    }

    @Bean
    public DBServiceMessageSystemClient getDBServiceMessageSystemClient(MessageSystem messageSystem, JdbcTemplate<User> jdbcTemplate) {
        DBServiceMessageSystemClient dbServiceMessageSystemClient = new DBServiceMessageSystemClientImpl(messageSystem, jdbcTemplate);
        dbServiceMessageSystemClient.setAddress(new Address(dbServiceAddress));
        messageSystem.addMessageSystemClient(dbServiceMessageSystemClient);
        return dbServiceMessageSystemClient;
    }

}
